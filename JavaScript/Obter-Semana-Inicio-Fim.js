function obterDiadaSemana() {
    var hj = new Date();
    var diasemana = hj.getDay();

    var ultimosabado = diasemana + 1;
    var ultimodomingo = diasemana + 7;

    var lastSaturday = new Date(hj);
    lastSaturday.setDate(hj.getDate() - ultimosabado);
    var lastSunday = new Date(hj);
    lastSunday.setDate(hj.getDate() - ultimodomingo);

    var colocarzeros = function(n) {
        return n < 10 ? '0' + n : n;
    };

    var formatarData = function(dt) {
        var d = colocarzeros(dt.getDate());
        var m = colocarzeros(dt.getMonth() + 1);
        var y = dt.getFullYear();
        return d + '/' + m + '/' + y;
    };

    return {
        sbado: formatarData(lastSaturday),
        domingo: formatarData(lastSunday)
    };
}

//var ultimasemana = obterDiadaSemana();
//var dataSabado = '01/12/2023'
//var dataDomingo = '01/01/2023'

var ultimasemana = obterDiadaSemana();
var dataSabado = ultimasemana.sbado
var dataDomingo = ultimasemana.domingo

