function recurse(element) {
    var elements = element.childNodes

    if (elements.length <= 0) {
        return
    }

    console.log(element)
    for (let i = 0; i < elements.length; i++) {
        recurse(elements[i])
    }
}

var element = document.querySelector('body')
recurse(element)