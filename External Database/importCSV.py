import csv, sqlite3

con = sqlite3.connect("internaldatabase.db")
cur = con.cursor()

#cur.execute("CREATE TABLE t3 (name, productnum,category,imageName);")
tableName = "products2014_content"

with open('masterList.csv','rt') as fin: # `with` statement available in 2.5+
    # csv.DictReader uses first line in file for column headings by default
    dr = csv.DictReader(fin) # comma is default delimiter
    to_db = [(i['name'], i['productnum'],i['category'],i['imageName']) for i in dr]

cur.executemany("INSERT INTO "+ tableName + " (c0name, c1productnum,c2category,c3imageName) VALUES (?,?,?, ?);", to_db)
con.commit()
