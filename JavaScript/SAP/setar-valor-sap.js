function setSapValue(fieldId, value) {
    var oBatch = {};
    var oControl = window.frames[1][0].window.sap.its.getControlById(fieldId);
    var oData = oControl.getCustomData();
    window.frames[1][0].window.sap.its.oFieldHelpMgr.tempStoredValue = "";

    oBatch.content = value
    if (window.frames[1][0].window.sap.its.oFieldHelpMgr.LH_Enabled) {
        var historyId = window.frames[1][0].window.sap.its.oFieldHelpMgr.GetHistoryFieldId(fieldId);
        window.frames[1][0].window.sap.its.oFieldHelpMgr.AddHistoryValue(historyId, value);
    }

    oBatch.post = 'value/' + oData.SID;

    window.frames[1][0].window.sap.its.oCommMgr.addBatch(oBatch);
}