import { useState } from 'react'

function Condicional() {

    const [email, setEmail] = useState()
    const [userEmail, setUserEmail] = useState()

    function enviarEmail(e) {        
        e.preventDefault()        
        setUserEmail(email)
    }

    function limparEmail() {
        setUserEmail(undefined)
    }

    return (
        <div>
            <h2>Cadastre seu e-mail</h2>
            <form>
                <input type='email' placeholder='digite seu email' onChange={(e) => setEmail(e.target.value)}/>
                <button type='submit' onClick={enviarEmail}>Enviar email</button>
            </form>
            {userEmail && ( // quando a condição é true, é exibido tudo que está dentro do parenteses
                <div>
                    <p>O email do usuario é {userEmail}</p>
                    <button onClick={limparEmail}>Limpar email</button>
                </div>
            )}
        </div>
    )
}

export default Condicional