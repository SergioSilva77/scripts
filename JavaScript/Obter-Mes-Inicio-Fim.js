function obterDatasMes() {
    var hoje = new Date();
    var primeiroDia = new Date(hoje.getFullYear(), hoje.getMonth(), 1);
    var ultimoDia = new Date(hoje.getFullYear(), hoje.getMonth() + 1, 0);

    var colocarZeros = function(n) {
        return n < 10 ? '0' + n : n;
    };

    var formatarData = function(dt) {
        var d = colocarZeros(dt.getDate());
        var m = colocarZeros(dt.getMonth() + 1);
        var y = dt.getFullYear();
        return d + '/' + m + '/' + y;
    };

    return {
        inicio: formatarData(primeiroDia),
        fim: formatarData(ultimoDia)
    };
}

// Uso da função
var datasMes = obterDatasMes();
var dataInicio = datasMes.inicio;
var dataFim = datasMes.fim;

console.log('Data de início do mês:', dataInicio);
console.log('Data de fim do mês:', dataFim);
