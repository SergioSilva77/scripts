create table data(
    name varchar,
    year integer,
    salary double
);

insert into data values 
('a',23, 4100),
('b',22, 1000),
('c',17, 2000);

-- aqui faz a recursao e consigo determinar um limite para essa recusao atraves do v.N < v.maxn

With Recursive tablecount AS (
   Select count(*) as maxn from data
), myvalues AS (
   Select name, year, salary, maxn as N, maxn
   From data d Inner Join tablecount c
  Union All
   Select v.name, v.year, v.salary, v.N, v.maxn
   From data d inner join myvalues v
     On d.name = v.name
   Where v.N<v.maxn -- determina o limite
)
Select name, year, salary
From myvalues;