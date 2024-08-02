function queryElement(myDocument, className, partialId, searchContent) {
    var queryResult = myDocument.getElementsByTagName(className);
    for (var index = 0; index < queryResult.length; index++) {
        var element = queryResult[index];

        if (searchContent) {
            if (element.innerText == partialId)
                return element;
        }
        else {
            if (element.id.endsWith(partialId))
                return element;
        }
    }
    return null;
}

function findRecursive(className, partialId, searchContent, iframe) {
    var myWindow = window;

    if (iframe) {
        myWindow = iframe.window;
    }

    var myDocument = myWindow.document;
    var myQuery = queryElement(myDocument, className, partialId, searchContent)
    if (!myQuery) {
        for (var index = 0; index < myWindow.frames.length; index++) {
            var element = myWindow.frames[index];

            myQuery = findRecursive(className, partialId, searchContent, element)
            if (myQuery) {
                return myQuery;
            }
        }
    }
    return myQuery;
}

var input = findRecursive('input', '_nome_pessoa')
if (!input){
    console.log('erro')
}
console.log(input.value)