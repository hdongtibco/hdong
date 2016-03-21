<?xml version='1.0' encoding='utf-8' ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" >
<xsl:output method="text" media-type="text/plain"/>
    <xsl:param name="feature_id" select="'feature.id.not.set.meaningless.result'" />
    <xsl:template match="plugin"><xsl:text>plugin_</xsl:text><xsl:value-of select="$feature_id" /><xsl:text>-</xsl:text><xsl:value-of select="@id" /><xsl:text>=</xsl:text><xsl:value-of select="@version"/><xsl:text>
</xsl:text>
    </xsl:template>
</xsl:stylesheet>