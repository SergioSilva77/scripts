const PDFMerge = require('pdf-merge');
const fs = require('fs')

var metodos = {

    // javascript
    'jsUtil': async function (browser, page, action) {
        var selector = action.selector
        var type = action.typeSelector
        var actionScript = action.actionScript
        var value = action.value

        return await page.evaluate((selector, type, actionScript, value) => {
            switch (actionScript) {
                case "setValue": {
                    var element = findRecursive(selector, type)
                    element.value = value
                }
                    break;
                case "setInnerText": {
                    var element = findRecursive(selector, type)
                    element.innerText = value
                }
                    break;
                case "setHtml": {
                    var element = findRecursive(selector, type)
                    element.innerHTML = value
                }
                    break;
                case "getValue": {
                    var element = findRecursive(selector, type)
                    return element.value
                }
                case "getHtml": {
                    var element = findRecursive(selector, type)
                    return element.innerHTML
                }
                case "getInnerText": {
                    var element = findRecursive(selector, type)
                    return element.innerText
                }
                case "click": {
                    var element = findRecursive(selector, type)
                    element.click()
                }
                    break;
                case "elementExists": {
                    var element = findRecursive(selector, type)
                    return element != null && element !== undefined && element != 'Tipo de seletor invalido'
                }
                case "getXpath": {
                    var element = findRecursive(selector, type)
                    return getXpathElement(element)
                }
                case "getAttribute": {
                    var element = findRecursive(selector, type)
                    return element.getAttribute(attribute)
                }
                case "obterLinksDasColunasTabela": { //parametros fixos, alterar futuramente
                    var links = []
                    var table = findRecursive('#frmApp', 'selector')
                    table = table.contentDocument.body.querySelectorAll('table')[2]
                    var rows = table.getElementsByTagName('tr')
                    console.log('oi')
                    for (let i = 1; i < rows.length; i++) {
                        const row = rows[i];
                        const link = getXpathElement(row.querySelector('a'))
                        const text = row?.querySelector('td').innerText
                        links.push({ "link": link, "text": text.replace(/[^\w\s]/gi, ' ') })
                    }
                    return links
                }
                case "obterDarfs": { //parametros fixos, alterar futuramente
                    var links = []
                    var rows = document.querySelector('#frmApp').contentDocument.querySelector('.dataGrid').getElementsByTagName('tr')

                    for (let i = 1; i < rows.length; i++) {
                        const row = rows[i];
                        var td = row.getElementsByTagName('td')[1]
                        const link = getXpathElement(td)
                        links.push(link)
                    }
                    return links
                }
                default:
                    break;
            }

            function getElementByXpath(doc, xpath) {
                var evaluator = new XPathEvaluator();
                var result = evaluator.evaluate(xpath, doc.documentElement, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null);
                return result.singleNodeValue
            }

            function getXpathElement(elm) {
                var allNodes = document.getElementsByTagName('*');
                for (var segs = []; elm && elm.nodeType == 1; elm = elm.parentNode) {
                    if (elm.hasAttribute('id')) {
                        var uniqueIdCount = 0;
                        for (var n = 0; n < allNodes.length; n++) {
                            if (allNodes[n].hasAttribute('id') && allNodes[n].id == elm.id) uniqueIdCount++;
                            if (uniqueIdCount > 1) break;
                        };
                        if (uniqueIdCount == 1) {
                            segs.unshift('id("' + elm.getAttribute('id') + '")');
                            return segs.join('/')
                        } else {
                            segs.unshift(elm.localName.toLowerCase() + '[@id="' + elm.getAttribute('id') + '"]');
                        }
                    } else if (elm.hasAttribute('class')) {
                        segs.unshift(elm.localName.toLowerCase() + '[@class="' + elm.getAttribute('class') + '"]');
                    } else {
                        for (i = 1, sib = elm.previousSibling; sib; sib = sib.previousSibling) {
                            if (sib.localName == elm.localName) i++;
                        };
                        segs.unshift(elm.localName.toLowerCase() + '[' + i + ']');
                    };
                };
                return segs.length ? '/' + segs.join('/') : null
            }

            function findRecursive(selector, type, iframe) {
                var myWindow = iframe ? iframe : window.document;
                var iframes = [myWindow, ...myWindow.getElementsByTagName('iframe')]

                for (let i = 0; i < iframes.length; i++) {
                    const iframe = iframes[i];
                    switch (type) {
                        case 'xpath': {
                            if (iframe.tagName != 'IFRAME') {
                                const element = getElementByXpath(iframe, selector)
                                if (element) {
                                    return element
                                }
                                continue
                            }
                            const doc = iframe.contentDocument
                            const element = getElementByXpath(doc, selector)
                            if (element) {
                                return element
                            }
                        }
                            break;
                        case 'selector': {
                            if (iframe.tagName != 'IFRAME') {
                                const element = iframe?.querySelector(selector)
                                if (element) {
                                    return element
                                }
                                continue
                            }
                            const doc = iframe.contentDocument
                            const element = doc?.querySelector(selector)
                            if (element) {
                                return element
                            }
                        }
                            break;
                        case 'selectorAll': {
                            if (iframe.tagName != 'IFRAME') {
                                const elements = iframe?.querySelectorAll(selector)
                                if (elements) {
                                    return elements
                                }
                                continue
                            }
                            const doc = iframe.contentDocument
                            const elements = doc?.querySelectorAll(selector)
                            if (elements) {
                                return elements
                            }
                        }
                            break;
                        default:
                            return 'Tipo de seletor invalido'
                    }
                }
                return null
            }
        }, selector, type, actionScript, value)
    },

    // puppetter
    'mergePdfs': async function (browser, page, action) {
        let files = fs.readFileSync(action.path);

        PDFMerge(files, { output: 'merged.pdf', orientation: 'portrait', order: 'asc' })
            .then(console.log)
            .catch(console.error);
    },
    'obterTotalIndexDarf': async function (browser, page, action) {
        await page.waitForSelector('iframe');
        await page.on('dialog', async (dialog) => {
            await dialog.dismiss().catch(() => {
                console.log(dialog.message());
                return new Result(TestStatus.FAIL, dialog.message())
            });
        })
        var frame = page.frames().find(frame => frame.name() === 'frmApp')
        // const elementHandle = await page.$('iframe[id="frmApp"]');

        var table = await frame.$('.dataGrid')
        var inputs = await table.$$('tr > td:nth-child(2) > input')
        return { index: inputs.length }
    },
    'pupBaixarDarfs': async function (browser, page, action) {
        await page.waitForSelector('iframe');
        // await page.on('dialog', async (dialog) => {
        //     await dialog.dismiss().catch(() => {
        //         console.log(dialog.message());
        //         return new Result(TestStatus.FAIL, dialog.message())
        //     });
        // })
        var frame = page.frames().find(frame => frame.name() === 'frmApp')
        // const elementHandle = await page.$('iframe[id="frmApp"]');

        var table = await frame.$('.dataGrid')
        var inputs = await table.$$('tr > td:nth-child(2) > input')
        inputs[action.index].click()
    },
    'sleep': async function (ms) {
        return new Promise(resolve => setTimeout(resolve, ms))
    },
    'pupCloseBrowser': async function (browser, page, action) {
        await browser.close()
    },
    'pupWaitElement': async function (browser, page, action) {
        await page.waitForSelector(action.selector, { visible: true, timetou: action.timeout })
    },
    'pupWaitElementInIframe': async function (browser, page, action) {
        var frame = page.frames().find(frame => frame.name() === action.frameId)
        await frame.waitForSelector(action.selector, { visible: true, timeout: action.timeout })
    },
    'pupJsSetHeigthPage': async function (browser, page, action) {
        const heigth = await page.evaluate(async (selector) => {
            return await new Promise(resolve => {
                resolve(document.querySelector(selector).contentDocument.body.clientHeight + document.body.clientHeight)
            })
        }, action.selector)
        console.log(heigth)
        await page.setViewport({
            width: action.width,
            height: heigth
        })
    },
    'pupSetViewPort': async function (browser, page, width, height) {
        await page.setViewport({
            width: width,
            height: height
        })
    },
    'pupScreenshotElement': async function (browser, page, action) {
        var element = await metodos.pupFindElementRecursive(browser, page, page.mainFrame(), action.selector, action.typeSelector)

        const box = await element.boundingBox();
        await page.screenshot({
            path: action.path,
            clip: {
                x: box.x,
                y: box.y,
                width: box.width,
                height: box.height
            }
        });
        return element !== null
    },
    'pupPageToPdf': async function (browser, page, action) {
        await page.pdf({
            path: action.path,
            format: 'A4',
            landscape: true,
            printBackground: true,
            displayHeaderFooter: true,
            fullPage: true
        });
    },
    'pupSwitchFrameId': function (browser, page, action) {
        return page.frames().find(frame => frame.name() === action.frameId);
    },
    'pupClickElement': async function (browser, page, action) {
        const element = await metodos.pupFindElementRecursive(browser, page, page.mainFrame(), action.selector, action.typeSelector);
        await element.click()
    },
    'pupClickElementSettimeout': async function (browser, page, action) {
        setTimeout(async () => {
            const element = await metodos.pupFindElementRecursive(browser, page, page.mainFrame(), action.selector, action.typeSelector);
            await element.click()
        }, action.timeout);
    },
    'pupGetTextElement': async function (browser, page, action) {
        var element = await metodos.pupFindElementRecursive(browser, page, page.mainFrame(), action.selector, action.typeSelector)
        return await page.evaluate(el => el.textContent, element)
    },
    'pupSetValueElement': async function (browser, page, action) {
        var element = await metodos.pupFindElementRecursive(browser, page, page.mainFrame(), action.selector, action.typeSelector)
        await element.type(action.value);
        return page
    },
    'pupFindElementRecursive': async function (browser, page, frame, selector, type, waitElement, results) {
        var element = null
        switch (type) {
            case 'xpath':
                element = await frame.$x(selector)
                break;
            case 'selector':
                element = await frame.$(selector)
                break;
            case 'selectorAll':
                element = await frame.$$(selector)
                break;
            default:
                return null
        }

        if (!results) {
            try {
                await frame.waitForSelector(selector, { visible: true, timeout: 10000 })
            } catch { }
            results = []
        }
        if (element) {
            results.push(element)
            return results.filter(i => i)[0]
        }
        for (const child of frame.childFrames()) {
            await metodos.pupFindElementRecursive(browser, page, child, selector, type, waitElement, results)
        }
        return results.filter(i => i)[0]
    },
    'pupSetDefaultLocationDownload': async function (browser, page, action) {
        // await browser.setDownloadBehavior({ behavior: 'save', downloadPath: action.path });
        const client = await page.target().createCDPSession()
        await client.send('Page.setDownloadBehavior', {
            behavior: 'allow',
            downloadPath: action.path,
        })
    },
    'goTo': async function (browser, page, action) {
        await page.goto(action.url);
        await page.setViewport({
            width: 1200,
            height: 3000
        })
        if (action.size) {
            await metodos.pupSetViewPort(browser, page, action.width, action.height)
        }
    },
    'jsExecuteJavaScript': async function (browser, page, action) {
        await page.evaluate(`${action.script}`);
    },
    'pupExecuteScript': function (browser, page, action) {
        eval(action.script)
    }
}

var funcoes = {
    'jsFindElementRecursive': async function (browser, page) { // cria a função de recursão para achar elementos em camadas
        await page.evaluate(() => {
            window.findRecursive = function (selector, type, iframe) {
                var myWindow = iframe ? iframe : window.document;
                var iframes = [myWindow, ...myWindow.getElementsByTagName('iframe')]

                for (let i = 0; i < iframes.length; i++) {
                    const iframe = iframes[i];
                    switch (type) {
                        case 'xpath': {
                            if (iframe.tagName != 'IFRAME') {
                                const element = window.getElementByXpath(iframe, selector)
                                if (element) {
                                    return element
                                }
                                continue
                            }
                            const doc = iframe.contentDocument
                            const element = window.getElementByXpath(doc, selector)
                            if (element) {
                                return element
                            }
                        }
                            break;
                        case 'selector': {
                            if (iframe.tagName != 'IFRAME') {
                                const element = iframe?.querySelector(selector)
                                if (element) {
                                    return element
                                }
                                continue
                            }
                            const doc = iframe.contentDocument
                            const element = doc?.querySelector(selector)
                            if (element) {
                                return element
                            }
                        }
                            break;
                        case 'selectorAll': {
                            if (iframe.tagName != 'IFRAME') {
                                const elements = iframe?.querySelectorAll(selector)
                                if (elements) {
                                    return elements
                                }
                                continue
                            }
                            const doc = iframe.contentDocument
                            const elements = doc?.querySelectorAll(selector)
                            if (elements) {
                                return elements
                            }
                        }
                            break;
                        default:
                            return 'Tipo de seletor invalido'
                    }


                }
                return null
            }
        })
    },
    'jsGetXpathElement': async function (browser, page) {
        await page.evaluate(() => {
            window.getXpathElement = function (elm) {
                var allNodes = document.getElementsByTagName('*');
                for (var segs = []; elm && elm.nodeType == 1; elm = elm.parentNode) {
                    if (elm.hasAttribute('id')) {
                        var uniqueIdCount = 0;
                        for (var n = 0; n < allNodes.length; n++) {
                            if (allNodes[n].hasAttribute('id') && allNodes[n].id == elm.id) uniqueIdCount++;
                            if (uniqueIdCount > 1) break;
                        };
                        if (uniqueIdCount == 1) {
                            segs.unshift('id("' + elm.getAttribute('id') + '")');
                            return segs.join('/')
                        } else {
                            segs.unshift(elm.localName.toLowerCase() + '[@id="' + elm.getAttribute('id') + '"]');
                        }
                    } else if (elm.hasAttribute('class')) {
                        segs.unshift(elm.localName.toLowerCase() + '[@class="' + elm.getAttribute('class') + '"]');
                    } else {
                        for (i = 1, sib = elm.previousSibling; sib; sib = sib.previousSibling) {
                            if (sib.localName == elm.localName) i++;
                        };
                        segs.unshift(elm.localName.toLowerCase() + '[' + i + ']');
                    };
                };
                return segs.length ? '/' + segs.join('/') : null
            }
        })
    },
    'jsGetElementByXpath': async function (browser, page) {
        await page.evaluate(() => {
            window.getElementByXpath = function (doc, xpath) {
                var evaluator = new XPathEvaluator();
                var result = evaluator.evaluate(xpath, doc.documentElement, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null);
                return result.singleNodeValue
            }
        })
    },
    'jsCheckIfElementExists': async function (browser, page) {
        return await page.evaluate(() => {
            window.checkIfElementExists = function (selector, type) {
                var element = window.findRecursive(selector, type)
                return element != null && element !== undefined && element != 'Tipo de seletor invalido'
            }
        })
    },
    'jsClickElement': async function (browser, page) {
        return await page.evaluate(() => {
            window.clickInElement = function (selector, type) {
                var element = window.findRecursive(selector, type)
                element.click()
            }
        })
    },
    'jsSetValue': async function (browser, page) {
        await page.evaluate(() => {
            window.setValue = function (selector, type, value) {
                var element = window.findRecursive(selector, type)
                element.value = value
            }
        })
    },
    'jsSetInnerText': async function (browser, page) {
        await page.evaluate(() => {
            window.setInnerText = function (selector, type, value) {
                var element = window.findRecursive(selector, type)
                element.innerText = value
            }
        })
    },
    'jsGetAttributeElement': async function (browser, page) {
        await page.evaluate(() => {
            window.getAttributeElement = function (selector, type, attribute) {
                var element = window.findRecursive(selector, type)
                element.getAttribute(attribute)
            }
        })
    },
    'jsGetHtmlElement': async function (browser, page) {
        await page.evaluate(() => {
            window.getHtmlElement = function (selector, type) {
                var element = window.findRecursive(selector, type)
                return element.innerHTML
            }
        })
    },
    'jsGetTextElement': async function (browser, page) {
        await page.evaluate(() => {
            window.getTextElement = function (selector, type) {
                var element = window.findRecursive(selector, type)
                return element.innerText
            }
        })
    },
    'jsGetValueElement': async function (browser, page) {
        await page.evaluate(() => {
            window.geValueElement = function (selector, type) {
                var element = window.findRecursive(selector, type)
                return element.value
            }
        })
    },
}

async function load(browser, page) {
    // carregar antes todas funcoes no window
    var funcoesKeys = Object.keys(funcoes)
    for (let index = 0; index < funcoesKeys.length; index++) {
        const keyFuncao = funcoesKeys[index];
        const funcao = funcoes[keyFuncao]
        await funcao(browser, page)
    }
}

module.exports = { metodos, funcoes, load }
