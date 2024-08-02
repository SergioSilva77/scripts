function recursive(iframe, arr) {
    var myWindow = window;

    if (iframe) {
        myWindow = iframe.window;
    }

    if (!arr) arr = []

    var myDocument = myWindow.document;
    arr.push(myDocument)

    var iframes = myWindow.frames
    for (let i = 0; i < iframes.length; i++) {
        const element = iframes[i];
        recursive(element, arr)
    }

    return arr;
}

var iframes = recursive()
console.log(iframes)