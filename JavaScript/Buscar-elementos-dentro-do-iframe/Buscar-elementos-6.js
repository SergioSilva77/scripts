function stringEndsWith(myString, value) {
    return myString.indexOf(value, myString.length - value.length) !== -1;
}

function queryElement(myDocument, tagName, value, searchType, skipCount) {

    var currentSkip = 0;
    var totalSkip = 0;
    if (skipCount)
        totalSkip = skipCount;

    var queryResult = myDocument.getElementsByTagName(tagName);
    for (var index = 0; index < queryResult.length; index++) {
        var element = queryResult[index];

        switch (searchType) {
            case 'id':
                if (stringEndsWith(element.id, value)) {
                    if (currentSkip >= totalSkip)
                        return element;

                    currentSkip = currentSkip + 1;
                }
                break;
            case 'content':
                if (element.textContent == value) {
                    if (currentSkip >= totalSkip)
                        return element;

                    currentSkip = currentSkip + 1;
                }
                break;

            case 'title':
                if (element.title == value) {
                    if (currentSkip >= totalSkip)
                        return element;

                    currentSkip = currentSkip + 1;
                }
                break;

            default:
                if (stringEndsWith(element.id, value)) {
                    if (currentSkip >= totalSkip)
                        return element;

                    currentSkip = currentSkip + 1;
                }
                break;
        }
    }
    return null;
}

function findRecursive(tagName, value, searchContent, iframe, skipCount) {
    var myWindow = window;

    if (iframe) {
        myWindow = iframe.window;
    }
    var myQuery = null;

    var myDocument = myWindow.document;
    try {
        myQuery = queryElement(myDocument, tagName, value, searchContent, skipCount)
    } catch (e) { };
    if (!myQuery) {
        for (var index = 0; index < myWindow.frames.length; index++) {
            var element = myWindow.frames[index];

            try {
                myQuery = findRecursive(tagName, value, searchContent, element, skipCount)
            } catch (e) { };

            if (myQuery) {
                return myQuery;
            }
        }
    }
    return myQuery;
}

try{

    var btnNovaSolicitacao = findRecursive('a', 'bla', 'title');
    if (!btnNovaSolicitacao)
        return 'blah';
    btnNovaSolicitacao.click();
    return 'sucesso'; 
      
} catch (e) { 
    return e;
};