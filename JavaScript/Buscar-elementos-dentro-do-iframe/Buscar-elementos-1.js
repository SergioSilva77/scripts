function queryElement(myDocument, tagName, attribute, value){
    var tags = myDocument.getElementsByTagName(tagName)
    for (var i = 0; i < tags.length; i++) {
        const tag = tags[i];
        if (attribute == 'innerText') {
            if (tag.innerText.includes(value)) {
                return tag
            }
        } else if (attribute == 'innerHTML'){
            if (tag.innerHTML.includes(value)) {
                return tag
            }
        } else {
            if (String(tag.getAttribute(attribute)).includes(value)) {
                return tag
            }
        }
    }
    return null
}

function findRecursive(tagName, attribute, value, iframe) {
    var myWindow = window;

    if (iframe) {
        myWindow = iframe.window;
    }

    var myDocument =  myWindow.document;
    var myQuery = queryElement(myDocument,tagName, attribute, value)

    for (var i = 0; i < myWindow.frames.length; i++) {
        var element = myWindow.frames[i];        
        myQuery = findRecursive(tagName, attribute, value, element)
        if (myQuery) {
            break
        }
    }
    return myQuery;
}