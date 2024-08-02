function msg(){
	function enviar(){
		console.log('enviada')
		return 'sucesso'
	}

	return {
		enviar: enviar
	}
}

var m = msg()
m.enviar()