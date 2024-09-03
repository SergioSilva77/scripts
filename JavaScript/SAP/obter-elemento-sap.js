function findSAP(fieldId, iframe) {
    var myFrame = window;
    if (iframe) {
        myFrame = iframe;
    }

    try {
        var oControl = myFrame.window.sap.its.getControlById(fieldId);
        if (!!oControl)
            return myFrame;
    } catch (e) {};

    for (var index = 0; index < myFrame.frames.length; index++) {
        var fram = myFrame.frames[index];
        try {
            var frame = findSAP(fieldId, fram);
            if (frame) {
                return frame;
            }
        } catch (e) {};
    }

    return null;
}