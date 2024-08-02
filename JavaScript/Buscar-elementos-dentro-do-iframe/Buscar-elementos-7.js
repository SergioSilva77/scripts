function queryElement(myDocument, tagName, attribute, value){
    var tags = myDocument.getElementsByTagName(tagName)
    for (var i = 0; i < tags.length; i++) {
        var tag = tags[i];
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
        if (element.then === undefined) continue 
        myQuery = findRecursive(tagName, attribute, value, element)
        if (myQuery) {
            break
        }
    }
    return myQuery;
}

async function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

async function wait(tagName, attribute, value, time, limit) {
    limit = limit * 1000
    var endDate = new Date(Date.now()+limit)
    var startDate = new Date()
    
    while (startDate<=endDate) {
        startDate = new Date()
        var element = findRecursive(tagName, attribute, value)
        if (element) {
            return element
        }        
        await sleep(time)
    }
    return null
}

var element = await wait('span', 'innerText', '5 principais diferenças entre o Amor e a Paixão', 100, 10)
if (!element) {
    return 'elemento nao existe'
}

element.click()