var groupBy = function (xs, key1, key2) {
    return xs.reduce(function (rv, x) {
        (rv[x[key1] + ',' + x[key2]] = rv[x[key1] + ',' + x[key2]] || []).push(x);
        return rv;
    }, {});
};

groupBy([{'nome':'sergio', 'idade':23},{'nome':'lais', 'idade':22},{'nome':'sergio', 'idade':23},{'nome':'lais', 'idade':22}],'nome')

/*retorno 

{
    "sergio,undefined": [
        {
            "nome": "sergio",
            "idade": 23
        },
        {
            "nome": "sergio",
            "idade": 23
        }
    ],
    "lais,undefined": [
        {
            "nome": "lais",
            "idade": 22
        },
        {
            "nome": "lais",
            "idade": 22
        }
    ]
}*/