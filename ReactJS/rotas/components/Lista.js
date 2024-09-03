function Lista() {
    var veiculos = ['Carro','Moto','Bicicleta']
    return (<>
        <h2>Lista de veiculos</h2>
        {veiculos.length > 0 ? (veiculos.map((value,index) =>(            
            <p key={index}>{value}</p>
        ))) : (
            <p>Não há itens na lista</p>
        )}
        {/* acima temos uma condiçção ternaria, é a mesma coisa que veiculos.length > 0 ? true : false */}
    </>)
}

export default Lista