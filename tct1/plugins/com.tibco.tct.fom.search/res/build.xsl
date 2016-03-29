<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xalan="http://xml.apache.org/xalan">
	<xsl:output method="text" indent="yes" xalan:indent-amount="2" />
    <xsl:param name="tibcoHome"/>

	<xsl:template match="/">
tibco.home=<xsl:value-of select="$tibcoHome"/>
plugins.home=<xsl:value-of select="fom/search/pluginhome" />

# FOS Properties File Info
search.file.path=<xsl:value-of select="fom/search/filepath" />
search.properties.info=<xsl:value-of select="fom/search/propertiesInfo" />
    </xsl:template>
</xsl:stylesheet>