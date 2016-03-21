<?xml version='1.0' encoding='utf-8' ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" >
<xsl:output method="text" media-type="text/plain"/>
    <xsl:param name="propname" select="'last.revision'" />
    <xsl:template match="/"><xsl:value-of select="$propname" /><xsl:text>=</xsl:text><xsl:value-of select="info/entry/commit/@revision"/><xsl:text>
</xsl:text>
    </xsl:template>
</xsl:stylesheet>