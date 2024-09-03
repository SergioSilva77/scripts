public static List<List<T>> DividirListaEmLotes<T>(List<T> originalList, int numberOfParts)
{
    if (numberOfParts <= 0)
    {
        throw new ArgumentException("O número de partes deve ser maior que zero.", nameof(numberOfParts));
    }

    List<List<T>> result = new List<List<T>>();

    int totalSize = originalList.Count;
    int partSize = totalSize / numberOfParts;
    int remainder = totalSize % numberOfParts;

    int currentIndex = 0;

    for (int i = 0; i < numberOfParts; i++)
    {
        int currentPartSize = partSize + (remainder > 0 ? 1 : 0);
        remainder--;

        List<T> part = originalList.Skip(currentIndex).Take(currentPartSize).ToList();
        result.Add(part);
        currentIndex += currentPartSize;
    }

    return result;
}