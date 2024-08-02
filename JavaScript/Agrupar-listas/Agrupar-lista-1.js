var data = [{"name":1},{"name":1},{"name":2},{"name":2},{"name":3}]

groupBy(data, 'name','age','gender')


function groupBy(arr, ...keys) {
    const result = {};
    arr.forEach(obj => {
      let current = result;
      keys.forEach(key => {
        const value = obj[key];
        current[value] = current[value] || {};
        current = current[value];
      });
      current.values = current.values || [];
      current.values.push(obj);
    });
    return result;
  }