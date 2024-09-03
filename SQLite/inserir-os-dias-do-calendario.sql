CREATE TABLE IF NOT EXISTS calendar (
  d date UNIQUE NOT NULL,
  dayofweek INT NOT NULL,
  weekday TEXT NOT NULL,
  quarter INT NOT NULL,
  year INT NOT NULL,
  month INT NOT NULL,
  day INT NOT NULL
);

select * from calendar;

INSERT
  OR ignore INTO calendar (d, dayofweek, weekday, quarter, year, month, day)
SELECT *
FROM (
  WITH RECURSIVE dates(d) AS (
    VALUES('1980-01-01')
    UNION ALL
    SELECT date(d, '+1 day')
    FROM dates
    WHERE d < '2039-01-01'
  )
  SELECT d,
    (CAST(strftime('%w', d) AS INT) + 6) % 7 AS dayofweek,
    CASE
      (CAST(strftime('%w', d) AS INT) + 6) % 7
      WHEN 0 THEN 'Monday'
      WHEN 2 THEN 'Wednesday'
      WHEN 3 THEN 'Thursday'
      WHEN 1 THEN 'Tuesday'
      WHEN 4 THEN 'Friday'
      WHEN 5 THEN 'Saturday'
      ELSE 'Sunday'
    END AS weekday,
    CASE
      WHEN CAST(strftime('%m', d) AS INT) BETWEEN 1 AND 3 THEN 1
      WHEN CAST(strftime('%m', d) AS INT) BETWEEN 4 AND 6 THEN 2
      WHEN CAST(strftime('%m', d) AS INT) BETWEEN 7 AND 9 THEN 3
      ELSE 4
    END AS quarter,
    CAST(strftime('%Y', d) AS INT) AS year,
    CAST(strftime('%m', d) AS INT) AS month,
    CAST(strftime('%d', d) AS INT) AS day
  FROM dates
);