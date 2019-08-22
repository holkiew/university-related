# MD 2018 project
This project is focused on creating a multidimensional model from existing relational database for analytical purposes. It describes business model of a fictional company called Adventure Works Cycles. This company is a multinational bicycle manufacturer and it stores the sales data, customers data, and much more. Next part is about finding the best analytical tasks, which suits the business model most. Based on this analytical task is built model (schema) from existing relational model. In last part project is focused on implementation of this model, migrating data from relational database and comparing speed of defined analytical tasks in OLTP and OLAP.

## Structure
* **doc/** - documents regarding to projects (report, subject proposal)
* **src/oltp_database** - original dataset backup file & info
* **src/transformation** - transformation from OLTP to OLAP process files
* **src/starschema** - OLAP model script & populating scripts & info
