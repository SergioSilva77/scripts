import {useState} from 'react'

function FormUserState(){

    const [nome, setName] = useState('Sérgio') // toda vez que o usuario digita o valor é guardado aqui
    const [pass, setPass] = useState() // toda vez que o usuario digita o valor é guardado aqui

    function cadastrarUsuario(e){
        e.preventDefault() // para não atualizar a pagina em seguida quando clicar em Ok
        alert(`Usuario ${nome} senha: ${pass} cadastrado com sucesso!`)
    }

    return (<div>
        <h1>Meu cadastro</h1>
        <form onSubmit={cadastrarUsuario}>
            <div>
                <label htmlFor="name">Nome:</label>
                <input type='text' id='name' placeholder="digite o seu nome" onChange={function(e){setName(e.target.value)}}/>
            </div>
            <div>
                <label htmlFor="pass">Senha:</label>
                <input type='text' id='pass' placeholder="digite a sua senha" onChange={function(e){setPass(e.target.value)}}/>
            </div>
            <div>
                <button>Go</button>
            </div>
        </form>
    </div>)    
}


export default FormUserState