<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
	<xsl:param name="otherfile"/>

    <!-- Grab otherfile as xsl variable -->
    <xsl:variable name="otherDoc" select="document($otherfile)"/>

	<xsl:template match="/site-provenance">
		<xsl:copy>
			<!-- Copy element -->
			<!-- apply templates to all children -->
			<xsl:apply-templates select="@* | node()"/>

			<xsl:apply-templates select="$otherDoc//site-provenance/*"/>
		</xsl:copy>
	</xsl:template>

	<!-- By default copy all attrs and elements verbatim. -->
	<xsl:template match="@* | node() | text()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node() | text()"/>
		</xsl:copy>
	</xsl:template>

</xsl:stylesheet>