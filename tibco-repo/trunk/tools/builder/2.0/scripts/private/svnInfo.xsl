<?xml version='1.0' encoding='utf-8' ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" >
<xsl:output method="text" media-type="text/plain"/>
    <xsl:param name="repo-propname" select="'svn.repoURL'" />
    <xsl:param name="tags-propname" select="'svn.tagsURL'" />
    <xsl:param name="revurl-propname" select="'svn.revURL'" />
    <xsl:param name="revision-propname" select="'svn.revision'" />
    <xsl:param name="date-propname" select="'svn.date'" />
    <xsl:param name="tags-prefix" select="''" />
    <xsl:param name="tags-suffix" select="''" />
    <xsl:template match="/"><xsl:value-of select="$repo-propname" /><xsl:text>=</xsl:text><xsl:value-of select="/info/entry/repository/root"/><xsl:text>
</xsl:text><xsl:value-of select="$tags-propname" /><xsl:text>=</xsl:text><xsl:value-of select="/info/entry/repository/root" /><xsl:value-of select="$tags-prefix" /><xsl:text>/tags</xsl:text><xsl:value-of select="$tags-suffix" /><xsl:text>
</xsl:text><xsl:value-of select="$revurl-propname" /><xsl:text>=</xsl:text><xsl:value-of select="/info/entry/url" /><xsl:text>
</xsl:text><xsl:value-of select="$revision-propname" /><xsl:text>=</xsl:text><xsl:value-of select="/info/entry/@revision" /><xsl:text>
</xsl:text><xsl:value-of select="$date-propname" /><xsl:text>=</xsl:text><xsl:value-of select="/info/entry/commit/date" /><xsl:text>
</xsl:text>
    </xsl:template>
</xsl:stylesheet>
