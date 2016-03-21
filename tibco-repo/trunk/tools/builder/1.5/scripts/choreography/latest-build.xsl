<?xml version='1.0' encoding='utf-8' ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" >
    <xsl:template match="*">
        <xsl:copy>
              <xsl:copy-of select="@*"/>
              <xsl:apply-templates/>
          </xsl:copy>
    </xsl:template>

    <xsl:template match="project">
        <xsl:copy>
            <xsl:attribute name="name">
                <xsl:value-of select="concat(@name,' choreography')"/>
            </xsl:attribute>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>

    <!-- don't copy these bits (empty transform gives null) -->
    <xsl:template match="property[@name = 'autobuild.build.latest.extension-location']" />
    <xsl:template match="property[@name = 'autobuild.build.latest.update-site']" />
    <xsl:template match="property[@name = 'autobuild.build.latest.has-tests']" />

    <!-- change the value of this one -->
    <xsl:template match="import">
        <xsl:copy>
            <xsl:attribute name="file">
                <xsl:value-of select="'${autobuild.dir.project}/artifacts-list.xml'" />
            </xsl:attribute>
            <xsl:copy-of select="@optional" />
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>
