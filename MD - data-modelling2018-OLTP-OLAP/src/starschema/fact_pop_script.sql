use starschema;

DELETE FROM FactSalesReason;

INSERT INTO FactSalesReason
(SalesReasonKey, SalesOrderNumber)
SELECT
	s.SalesReasonKey,
	h.SalesOrderNumber
FROM DimSalesReason s
LEFT JOIN AdventureWorks2016.Sales.SalesOrderHeaderSalesReason r ON r.SalesReasonID = s.SalesReasonID
LEFT JOIN AdventureWorks2016.Sales.SalesOrderHeader h ON h.SalesOrderID = r.SalesOrderID
WHERE h.SalesOrderNumber is not null;

DECLARE @defCurrency int = 0;
SELECT  @defCurrency = c.CurrencyKey FROM DimCurrency c WHERE c.CurrencyRateID = 0;

DELETE FROM FactOnlineSales;

INSERT INTO FactOnlineSales(
	ProductKey,
	CustomerKey,
	CurrencyKey,
	SpecialOfferKey,
	OrderDateKey,
	DueDateKey,
	ShipDateKey,
	ShipToAddressKey,
	BillToAddressKey,
	SalesTerritoryKey,
	ShipMethodKey,
	SalesOrderNumber,
	SalesOrderDetailID,
	OrderQuantity,
	RevisionNumber,
	Status,
	UnitCost,
	UnitSellingPrice,
	UnitSellingPriceDiscount,
	SubTotal,
	TaxAmt,
	Freight)
SELECT
	ProductKey,
	CustomerKey,
	CASE WHEN CurrencyKey is NULL THEN @defCurrency ELSE CurrencyKey END,
	SpecialOfferKey,
	od.DateKey,
	dd.DateKey,
	sd.DateKey,
	sa.AddressKey,
	ba.AddressKey,
	SalesTerritoryKey,
	ShipMethodKey,
	SalesOrderNumber,
	SalesOrderDetailID,
	d.OrderQty,
	RevisionNumber,
	Status,
	p.StandardCost,
	d.UnitPrice,
	d.UnitPriceDiscount,
	SubTotal,
	TaxAmt,
	Freight
FROM 
   AdventureWorks2016.Sales.SalesOrderDetail d
LEFT JOIN AdventureWorks2016.Sales.SalesOrderHeader h ON h.SalesOrderID = d.SalesOrderID
LEFT JOIN DimProduct p ON p.ProductID = d.ProductID
LEFT JOIN DimCustomer c ON c.CustomerID = h.CustomerID
LEFT JOIN DimShipMethod s ON s.ShipMethodID = h.ShipMethodID
LEFT JOIN DimCurrency cr ON cr.CurrencyRateID = h.CurrencyRateID
LEFT JOIN DimSpecialOffer so ON so.SpecialOfferID = d.SpecialOfferID
LEFT JOIN DimAddress sa ON sa.AddressID = h.ShipToAddressID
LEFT JOIN DimAddress ba ON ba.AddressID = h.BillToAddressID
LEFT JOIN DimSalesTerritory t ON t.TerritoryID = h.TerritoryID
LEFT JOIN DimDate od ON od.Date = h.OrderDate
LEFT JOIN DimDate dd ON dd.Date = h.DueDate
LEFT JOIN DimDate sd ON sd.Date = h.ShipDate
Where h.OnlineOrderFlag = 1;

DELETE FROM FactRetailSales;

INSERT INTO FactRetailSales(
	ProductKey,
	CustomerKey,
	EmployeeKey,
	CurrencyKey,
	SpecialOfferKey,
	OrderDateKey,
	DueDateKey,
	ShipDateKey,
	ShipToAddressKey,
	BillToAddressKey,
	SalesTerritoryKey,
	ShipMethodKey,
	SalesOrderNumber,
	SalesOrderDetailID,
	OrderQuantity,
	RevisionNumber,
	Status,
	UnitCost,
	UnitSellingPrice,
	UnitSellingPriceDiscount,
	SubTotal,
	TaxAmt,
	Freight)
SELECT
	ProductKey,
	CustomerKey,
	EmployeeKey,
	CASE WHEN CurrencyKey is NULL THEN @defCurrency ELSE CurrencyKey END,
	SpecialOfferKey,
	od.DateKey,
	dd.DateKey,
	sd.DateKey,
	sa.AddressKey,
	ba.AddressKey,
	SalesTerritoryKey,
	ShipMethodKey,
	SalesOrderNumber,
	SalesOrderDetailID,
	d.OrderQty,
	RevisionNumber,
	Status,
	p.StandardCost,
	d.UnitPrice,
	d.UnitPriceDiscount,
	SubTotal,
	TaxAmt,
	Freight
FROM 
   AdventureWorks2016.Sales.SalesOrderDetail d
LEFT JOIN AdventureWorks2016.Sales.SalesOrderHeader h ON h.SalesOrderID = d.SalesOrderID
LEFT JOIN DimProduct p ON p.ProductID = d.ProductID
LEFT JOIN DimEmployee e ON e.BusinessEntityID = h.SalesPersonID
LEFT JOIN DimCustomer c ON c.CustomerID = h.CustomerID
LEFT JOIN DimShipMethod s ON s.ShipMethodID = h.ShipMethodID
LEFT JOIN DimCurrency cr ON cr.CurrencyRateID = h.CurrencyRateID
LEFT JOIN DimSpecialOffer so ON so.SpecialOfferID = d.SpecialOfferID
LEFT JOIN DimAddress sa ON sa.AddressID = h.ShipToAddressID
LEFT JOIN DimAddress ba ON ba.AddressID = h.BillToAddressID
LEFT JOIN DimSalesTerritory t ON t.TerritoryID = h.TerritoryID
LEFT JOIN DimDate od ON od.Date = h.OrderDate
LEFT JOIN DimDate dd ON dd.Date = h.DueDate
LEFT JOIN DimDate sd ON sd.Date = h.ShipDate
Where h.OnlineOrderFlag = 0;

DELETE FROM FactPurchaseOrder;

INSERT INTO FactPurchaseOrder
   (ProductKey,EmployeeKey,ShipMethodKey,VendorKey,	DueDateKey,ShipDateKey,OrderDateKey,PurchaseOrderID,
   PurchaseOrderDetailID,RevisionNumber,Status,OrderQty,UnitCost,ReceivedQty,RejectedQty,SubTotal,TaxAmt,Freight)
SELECT
	ProductKey,
	EmployeeKey,
	ShipMethodKey,
	VendorKey,	
	dd.DateKey,
	sd.DateKey,
	od.DateKey,	
	h.PurchaseOrderID,
	PurchaseOrderDetailID,
	RevisionNumber,
	Status,
	OrderQty,
	d.UnitPrice,
	ReceivedQty,
	RejectedQty,
	SubTotal,
	TaxAmt,
	Freight
FROM 
   AdventureWorks2016.Purchasing.PurchaseOrderDetail d
LEFT JOIN AdventureWorks2016.Purchasing.PurchaseOrderHeader h ON h.PurchaseOrderID = d.PurchaseOrderID
LEFT JOIN DimProduct p ON p.ProductID = d.ProductID
LEFT JOIN DimEmployee e ON e.BusinessEntityID = h.EmployeeID
LEFT JOIN DimShipMethod s ON s.ShipMethodID = h.ShipMethodID
LEFT JOIN DimVendor v ON v.BusinessEntityID = h.VendorID
LEFT JOIN DimDate dd ON dd.Date = d.DueDate
LEFT JOIN DimDate sd ON sd.Date = h.ShipDate
LEFT JOIN DimDate od ON od.Date = h.OrderDate;