function query(elemento, tag, contains_classe, contains_id, contains_texto, atributos) {
    if (!elemento) {
        return [false, null];
    }

    var resultado = [];

    if (tag) {
        const valTag = elemento.tagName.toLocaleLowerCase() == tag.toLocaleLowerCase();
        resultado.push(valTag);
    }

    if (atributos) {
        for (let i = 0; i < atributos.length; i++) {
            var atributo = atributos[i][0];
            var valor_atributo = atributos[i][1];
            var tipo = atributos[i][2];

            var resultAtributo = false;
            if (elemento.getAttribute(atributo)) {
                switch(tipo){
                    case "igual":
                        
                        resultAtributo = elemento.getAttribute(atributo)?.trim() === valor_atributo.trim();
                        break;
                    case "includes":
                        resultAtributo = elemento.getAttribute(atributo)?.includes(valor_atributo);
                        break;
                    default:
                        resultAtributo = false;
                        break;
                }
            }else{
                resultAtributo = false
            }
            resultado.push(resultAtributo);
        }
    }

    if (contains_classe) {
        const valClass = elemento.getAttribute('class')?.includes(contains_classe);
        resultado.push(valClass);
    }

    if (contains_id) {
        const valId = elemento.getAttribute('id')?.includes(contains_id);
        resultado.push(valId);
    }

    if (contains_texto) {
        var valTexto = false
        if (elemento.tagName.toLocaleLowerCase() == 'input') {
            valTexto = elemento.value.includes(contains_texto);
        }else{
            valTexto = elemento.innerHTML.includes(contains_texto);
        }
        resultado.push(valTexto);
    }

    resultado = !resultado.includes(false);

    return [resultado, elemento];
}

function obterTodosElementos(tag, classe, id, atributos, texto, root, encontrados) {
    if (!encontrados) {
        encontrados = [];
    }

    const elementos = Array.from(root.querySelectorAll('*'));

    for (let i = 0; i < elementos.length; i++) {
        const elemento = elementos[i];
        const shadow = elemento.shadowRoot;

        if (shadow) {
            // Se houver um shadowRoot, buscamos recursivamente dentro dele
            obterTodosElementos(tag, classe, id, atributos, texto, shadow, encontrados);
        }

        const filtroNormal = query(elemento, tag, classe, id, texto, atributos);
        if (filtroNormal[0]) {
            // Adiciona o elemento encontrado
            encontrados.push([root, elemento]);
        }
        
    }

    return encontrados;
}

const resultado = obterTodosElementos(
    //'history-searched-label.website-title', // seletor
    'input', // tag
    null, // classe
    null, // id
    [['formcontrolname', 'ag', 'igual'], ['mask', '0000', 'igual']], // atributo e valor
    null, // texto
    document, // documento atual
    [] // encontrados
);

console.log(resultado);
