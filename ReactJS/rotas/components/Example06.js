/* props */

import propTypes from 'prop-types'

function Example06({marca, ano_lancamento}){
    return (<>
        <label>Marca: {marca} - ano: {ano_lancamento}</label>
    </>)    
}

/*Indica para o programador os campos colocados abaixo terão que aceitar um tipo valido*/
Example06.propTypes = {
    marca: propTypes.string.isRequired,
    ano_lancamento: propTypes.number.isRequired
}

/*Indica o valor padrão das propriedades caso não seja colocado nenhum valor*/
Example06.defaultProps = {
    marca: 'faltou a marca',
    ano_lancamento: 0
}

export default Example06