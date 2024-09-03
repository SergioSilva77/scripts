function Forms({usuario}){

    function cadastrarUsuario(e){
        e.preventDefault() // para não atualizar a pagina em seguida quando clicar em Ok
        alert(`Usuario ${usuario} cadastrado com sucesso!`)
    }

    return (<div>
        <h1>Meu cadastro</h1>
        <form onSubmit={cadastrarUsuario}>
            <input type='text' placeholder="digite o seu nome"/>
            <input type='submit' value='cadastrar'/>
        </form>
    </div>)    
}


export default Forms