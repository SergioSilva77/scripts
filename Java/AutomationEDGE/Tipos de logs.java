public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws ProcessStudioException {
  if (first) {
    first = false;
  }

  Object[] r = getRow();
  if (r == null) {
    setOutputDone();
    return false;
  }
  r = createOutputRow(r, data.outputRowMeta.size());

  //#######################
  logBasic(">>>>>>>>>>>>>>>>>> log básico");
  logDebug(">>>>>>>>>>>>>>>>>> logDebug");
  logDetailed(">>>>>>>>>>>>>>>>>> logDetailed");
  logError(">>>>>>>>>>>>>>>>>> logError");
  logMinimal(">>>>>>>>>>>>>>>>>> logMinimal");
  logRowlevel(">>>>>>>>>>>>>>>>>> logRowlevel");
  logSummary();
  //#######################

  putRow(data.outputRowMeta, r);
  return true;
}