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
  putError(parent.getInputRowMeta(), r, 0, "descricao erro", "nome do campo", "codigo erro");
  //#######################

  putRow(data.outputRowMeta, r);
  return true;
}