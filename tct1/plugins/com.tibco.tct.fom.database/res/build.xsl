<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xalan="http://xml.apache.org/xalan">
	<xsl:output method="text" indent="yes" xalan:indent-amount="2" />
    <xsl:param name="tibcoHome"/>

	<xsl:template match="/">
tibco.home=<xsl:value-of select="$tibcoHome"/>
plugins.home=<xsl:value-of select="fom/databaseConfig/pluginhome" />

# Database Config Info
#db.host.name=<xsl:value-of select="fom/databaseConfig/host" />
#db.port=<xsl:value-of select="fom/databaseConfig/port" />
db.driver.path=<xsl:value-of select="fom/databaseConfig/driverLocation" />
db.hasLastestOjdbcDriver=<xsl:value-of select="fom/databaseConfig/hasLastestOjdbcDriver" />
db.url=<xsl:value-of select="fom/databaseConfig/dburl" />
db.user=<xsl:value-of select="fom/databaseConfig/user" />
db.password=<xsl:value-of select="fom/databaseConfig/password" />
#db.sid=<xsl:value-of select="fom/databaseConfig/sid" />

# FOM_TIBCO_HOME
fom.tibco.home=<xsl:value-of select="fom/CommonConfiguration/tibcofomhome" />
fom.tct.home=<xsl:value-of select="fom/CommonConfiguration/tcthome" />

# Create user
db.create.enable=<xsl:value-of select="fom/databaseConfig/createuser/createuserenable" />
db.create.username=<xsl:value-of select="fom/databaseConfig/createuser/createdusername" />
db.create.userpassword=<xsl:value-of select="fom/databaseConfig/createuser/createduserpassword" />
db.create.existtablespacename=<xsl:value-of select="fom/databaseConfig/createuser/existtablespacename" />
db.create.existtablespacesize=<xsl:value-of select="fom/databaseConfig/createuser/existtablespacesize" />

# Create tablespace
db.create.tablespaceenable=<xsl:value-of select="fom/databaseConfig/createtablespace/createtablespaceenable" />
db.create.tablespacename=<xsl:value-of select="fom/databaseConfig/createtablespace/tablespacename" />
db.create.tablespaceminsize=<xsl:value-of select="fom/databaseConfig/createtablespace/tablespaceminsize" />
db.create.tablespacemaxsize=<xsl:value-of select="fom/databaseConfig/createtablespace/tablespacemaxsize" />
db.create.tablespacelocation=<xsl:value-of select="fom/databaseConfig/createtablespace/tablespacelocation" />
db.create.tablespace.filename=<xsl:value-of select="fom/databaseConfig/createtablespace/tablespacedbfilename" />

db.run.script=<xsl:value-of select="fom/databaseConfig/runscript" />

    </xsl:template>
</xsl:stylesheet>