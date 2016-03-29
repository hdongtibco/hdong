<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xalan="http://xml.apache.org/xalan">
	<xsl:output method="text" indent="yes" xalan:indent-amount="2" />
    <xsl:param name="tibcoHome"/>

	<xsl:template match="/">
tibco.home=<xsl:value-of select="$tibcoHome"/>
plugins.home=<xsl:value-of select="fom/oms/pluginhome" />

# OMS Info
oms.http.port=<xsl:value-of select="fom/oms/httpport" />
oms.ws.port=<xsl:value-of select="fom/oms/wsport" />
fom.oms.host=<xsl:value-of select="fom/oms/omshost" />
fom.tibco.home=<xsl:value-of select="fom/CommonConfiguration/tibcofomhome" />
    </xsl:template>
</xsl:stylesheet>