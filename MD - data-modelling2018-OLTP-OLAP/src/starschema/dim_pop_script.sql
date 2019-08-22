use starschema;

INSERT INTO DimSalesReason (Name, ReasonType, SalesReasonID)
SELECT
     s.Name
    ,s.ReasonType
    ,s.SalesReasonID    
FROM
    AdventureWorks2016.Sales.SalesReason s;
	

INSERT INTO DimSpecialOffer (Category, Description, DiscountPct, EndDate, MaxQty, MinQty, SpecialOfferID, StartDate, Type)
SELECT
    s.Category,
    s.Description,
    s.DiscountPct,
	s.EndDate,
	s.MaxQty,
	s.MinQty,
	s.SpecialOfferID,
	s.StartDate,
	s.Type
FROM
    AdventureWorks2016.Sales.SpecialOffer s;


INSERT INTO DimCustomer (CustomerID, EmailPromotion, FirstName, MiddleName, LastName,
 NameStyle, PersonType,
  StoreName, Suffix, Title)
SELECT
	c.CustomerID, p.EmailPromotion, p.FirstName, p.MiddleName, p.LastName,
	p.NameStyle, p.PersonType, 
	s.Name, p.Suffix, p.Title
FROM
    AdventureWorks2016.Sales.Customer c
LEFT JOIN AdventureWorks2016.Person.Person p on c.PersonID = p.BusinessEntityID
LEFT JOIN AdventureWorks2016.Sales.Store s on c.StoreID = s.BusinessEntityID;


INSERT INTO DimEmployee(
		BirthDate, Bonus, BusinessEntityID, CommissionPct, CurrentFlag ,EmailPromotion, FirstName, Gender,
		HireDate,JobTitle,LastName, MaritalStatus, MiddleName,NameStyle,
		PersonType, SalariedFlag, SalesLastYear, SalesPersonFlag, SalesQuota, SalesYTD, SickLeaveHours,
		Suffix, Title, VacationHours)
SELECT
		e.BirthDate, s.Bonus, e.BusinessEntityID, s.CommissionPct, e.CurrentFlag, p.EmailPromotion, p.FirstName, e.Gender,
		e.HireDate, e.JobTitle, p.LastName, e.MaritalStatus, p.MiddleName, p.NameStyle,
		p.PersonType, e.SalariedFlag, s.SalesLastYear, CASE WHEN s.BusinessEntityID is NULL THEN 0 ELSE 1 END, s.SalesQuota,
		s.SalesYTD, e.SickLeaveHours, p.Suffix, p.Title, e.VacationHours
FROM
	AdventureWorks2016.HumanResources.Employee e
LEFT JOIN AdventureWorks2016.Sales.SalesPerson s on s.BusinessEntityID = e.BusinessEntityID
LEFT JOIN AdventureWorks2016.Person.Person p on e.BusinessEntityID = p.BusinessEntityID;


INSERT INTO DimVendor(ActiveFlag, CreditRating, Name, PreferredVendorStatus, BusinessEntityID)
SELECT
    v.ActiveFlag,
	v.CreditRating,
	v.Name,
	v.PreferredVendorStatus,
	v.BusinessEntityID
FROM
    AdventureWorks2016.Purchasing.Vendor v;
	
	
INSERT INTO DimShipMethod(Name, ShipBase, ShipMethodID, ShipRate)
SELECT
    s.Name,
	s.ShipBase,
	s.ShipMethodID,
	s.ShipRate
FROM
    AdventureWorks2016.Purchasing.ShipMethod s;
	

INSERT INTO DimSalesTerritory(CostLastYear, CostYTD, CountryRegionName, SalesLastYear, SalesTerritoryName, SalesYTD, TerritoryID, [Group])
SELECT
    t.CostLastYear,
	t.CostYTD,
	r.Name,
	t.SalesLastYear,
	t.Name,
	t.SalesYTD,
	t.TerritoryID,
	t.[Group]
FROM
    AdventureWorks2016.Sales.SalesTerritory t
LEFT JOIN AdventureWorks2016.Person.CountryRegion r on r.CountryRegionCode = t.CountryRegionCode;


INSERT INTO DimProduct(Class, Color, DaysToManufacture, DiscontinuedDate, FinishedGoodsFlag, ListPrice, MakeFlag, ProductCategoryName, ProductID,
			ProductLine, ProductModelName, ProductName, ProductNumber, ProductSubcategoryName, ReorderPoint,
			SafetyStockLevel, SellEndDate, SellStartDate, Size, SizeUnitMeasureName, StandardCost, Style, Weight, WeightUnitMeasureName)
SELECT
    p.Class, p.Color, p.DaysToManufacture, p.DiscontinuedDate, p.FinishedGoodsFlag, p.ListPrice, p.MakeFlag, pc.Name, p.ProductID,
	p.ProductLine, pm.Name, p.Name, p.ProductNumber, ps.Name, p.ReorderPoint,
	p.SafetyStockLevel, p.SellEndDate, p.SellStartDate, p.Size, s_um.Name, p.StandardCost, p.Style, p.Weight, w_um.Name
FROM
    AdventureWorks2016.Production.Product p
LEFT JOIN AdventureWorks2016.Production.UnitMeasure s_um on s_um.UnitMeasureCode = p.SizeUnitMeasureCode
LEFT JOIN AdventureWorks2016.Production.UnitMeasure w_um on w_um.UnitMeasureCode = p.WeightUnitMeasureCode
LEFT JOIN AdventureWorks2016.Production.ProductSubcategory ps on ps.ProductSubcategoryID = p.ProductSubcategoryID
LEFT JOIN AdventureWorks2016.Production.ProductCategory pc on pc.ProductCategoryID = ps.ProductCategoryID
LEFT JOIN AdventureWorks2016.Production.ProductModel pm on pm.ProductModelID = p.ProductModelID;


INSERT INTO DimCurrency(AverageRate, CurrencyRateDate, CurrencyRateID, EndOfDayRate, FromCurrencyName, ToCurrencyName)
SELECT
   cr.AverageRate,
   cr.CurrencyRateDate,
   cr.CurrencyRateID,
   cr.EndOfDayRate,
   f_c.Name,
   t_c.Name
FROM
    AdventureWorks2016.Sales.CurrencyRate cr
LEFT JOIN AdventureWorks2016.Sales.Currency f_c on f_c.CurrencyCode = cr.FromCurrencyCode
LEFT JOIN AdventureWorks2016.Sales.Currency t_c on t_c.CurrencyCode = cr.ToCurrencyCode;

INSERT INTO DimCurrency(CurrencyRateID) VALUES (0);

INSERT INTO DimAddress(
			AddressID, AddressLine1, AddressLine2, City, CountryRegionName, IsOnlyStateProvinceFlag, PostalCode,
			SpatialLocation, StateProvinceCode, StateProvinceName)
SELECT
   a.AddressID, a.AddressLine1, a.AddressLine2, a.City, cr.Name, sp.IsOnlyStateProvinceFlag, a.PostalCode,
	a.SpatialLocation, sp.StateProvinceCode, sp.Name
FROM
    AdventureWorks2016.Person.Address a
LEFT JOIN AdventureWorks2016.Person.StateProvince sp on sp.StateProvinceID = a.StateProvinceID
LEFT JOIN AdventureWorks2016.Person.CountryRegion cr on cr.CountryRegionCode = sp.CountryRegionCode;

GO
DECLARE @dayNo bigint = 0;
DECLARE @day date = DATEADD(d,@dayNo,'2010-01-01');
DECLARE @today date = GETDATE();
WHILE @day < @today BEGIN  
   INSERT INTO DimDate
   ([Date],[Month],[Year],[DayOfWeek],[Quarter])
   SELECT
		@day AS [Date],
		DATEPART(MONTH, @day) AS [Month],
		DATEPART(YEAR, @day) AS [Year],
		DATEPART(WEEKDAY, @day) AS [DayOfWeek],
		DATEPART(QUARTER, @day) AS [Quarter];
		SET @dayNo += 1;
		SET @day = DATEADD(d,@dayNo,'2010-01-01');
END;
GO








