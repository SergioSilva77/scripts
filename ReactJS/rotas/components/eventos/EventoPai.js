import Button from "./Button"

function EventoPai({event, text}){

    function evento1(){
        alert('parabens! vc ativou o primeiro evento do pai 1')
    }

    function evento2(){
        alert('parabens! vc ativou o primeiro evento do pai 2')
    }

    function evento3(){
        alert('parabens! vc ativou o primeiro evento do pai 3')
    }

    function evento4(){
        alert('parabens! vc ativou o primeiro evento do pai 4')
    }

    /* vc pode chamar o evento automaticamente colocando parenteses, ex: evento1()*/
    return (<>        
        <Button texto='evento1' evento={evento1} /> 
        <Button texto='evento2' evento={evento2} /> 
        <Button texto='evento3' evento={evento3} /> 
        <Button texto='evento4' evento={evento4} /> 
    </>)    
}


export default EventoPai