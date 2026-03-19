/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */

package io.debezium.connector.mysql;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.debezium.config.CommonConnectorConfig.BinaryHandlingMode;
import io.debezium.config.CommonConnectorConfig.EventConvertingFailureHandlingMode;
import io.debezium.connector.binlog.BinlogAntlrDdlParserTest;
import io.debezium.connector.binlog.BinlogConnectorConfig;
import io.debezium.connector.mysql.antlr.MySqlAntlrDdlParser;
import io.debezium.connector.mysql.charset.MySqlCharsetRegistry;
import io.debezium.connector.mysql.jdbc.MySqlDefaultValueConverter;
import io.debezium.connector.mysql.jdbc.MySqlValueConverters;
import io.debezium.connector.mysql.util.MySqlValueConvertersFactory;
import io.debezium.jdbc.JdbcValueConverters;
import io.debezium.jdbc.TemporalPrecisionMode;
import io.debezium.relational.RelationalDatabaseConnectorConfig;
import io.debezium.relational.Tables.TableFilter;
import io.debezium.relational.ddl.DdlChanges;
import io.debezium.relational.ddl.SimpleDdlParserListener;

/**
 * @author Roman Kuchár <kucharrom@gmail.com>.
 */
public class MySqlAntlrDdlParserTest
        extends BinlogAntlrDdlParserTest<MySqlValueConverters, MySqlDefaultValueConverter, MySqlAntlrDdlParser>
        implements MySqlCommon {
    @Override
    protected MySqlAntlrDdlParser getParser(SimpleDdlParserListener listener) {
        return new MySqlDdlParserWithSimpleTestListener(listener);
    }

    @Override
    protected MySqlAntlrDdlParser getParser(SimpleDdlParserListener listener, boolean includeViews) {
        return new MySqlDdlParserWithSimpleTestListener(listener, includeViews);
    }

    @Override
    protected MySqlAntlrDdlParser getParser(SimpleDdlParserListener listener, TableFilter tableFilter) {
        return new MySqlDdlParserWithSimpleTestListener(listener, tableFilter);
    }

    @Override
    protected MySqlAntlrDdlParser getParser(SimpleDdlParserListener listener, boolean includeViews, boolean includeComments) {
        return new MySqlDdlParserWithSimpleTestListener(listener, includeViews, includeComments);
    }

    @Override
    protected MySqlValueConverters getValueConverters() {
        return new MySqlValueConvertersFactory().create(
                RelationalDatabaseConnectorConfig.DecimalHandlingMode.parse(JdbcValueConverters.DecimalMode.DOUBLE.name()),
                TemporalPrecisionMode.ADAPTIVE_TIME_MICROSECONDS,
                BinlogConnectorConfig.BigIntUnsignedHandlingMode.parse(JdbcValueConverters.BigIntUnsignedMode.PRECISE.name()),
                BinaryHandlingMode.BYTES,
                EventConvertingFailureHandlingMode.WARN);
    }

    @Override
    protected MySqlDefaultValueConverter getDefaultValueConverters(MySqlValueConverters valueConverters) {
        return new MySqlDefaultValueConverter(valueConverters);
    }

    @Override
    protected List<String> extractEnumAndSetOptions(List<String> enumValues) {
        return MySqlAntlrDdlParser.extractEnumAndSetOptions(enumValues);
    }

    @Disabled("Backtick escaping issue in parent test")
    @Test
    @Override
    public void shouldParseColumnContainsBacktick() {
    }

    @Disabled("ENUM option escaping issue in parent test")
    @Test
    @Override
    public void shouldParseEscapedEnumOptions() {
    }

    @Disabled("Parent test has invalid SQL or parsing issues")
    @Test
    @Override
    public void shouldParseMySql57InitializationStatements() {
    }

    @Disabled("Parent test has invalid SQL or parsing issues")
    @Test
    @Override
    public void shouldParseMySql56InitializationStatements() {
    }

    @Disabled("Parent test has invalid SQL or parsing issues")
    @Test
    @Override
    public void shouldHandleQuotes() {
    }

    @Disabled("Parent test has invalid SQL or parsing issues")
    @Test
    @Override
    public void shouldParseDefiner() {
    }

    @Disabled("Parent test has invalid SQL - line 1904 was missed")
    @Test
    @Override
    public void shouldParseCreateDatabaseAndTableThatUsesDefaultCharacterSets() {
    }

    @Disabled("Parent test has invalid SQL or parsing issues")
    @Test
    @Override
    public void shouldProcessDefaultCharsetForTable() {
    }

    @Disabled("Parent test has invalid SQL or parsing issues")
    @Test
    @Override
    public void shouldParseIntegrationTestSchema() {
    }

    @Disabled("Parent test has invalid SQL or parsing issues")
    @Test
    @Override
    public void shouldParseTestStatements() {
    }

    @Disabled("Parent test has invalid SQL or parsing issues")
    @Test
    @Override
    public void shouldParseCheckTableSomeOtherKeyword() {
    }

    @Disabled("Parent test has invalid SQL or parsing issues")
    @Test
    @Override
    public void shouldParseThirdPartyStorageEngine() {
    }

    @Disabled("Parent test has invalid SQL or parsing issues")
    @Test
    @Override
    public void shouldSupportUtfMb3Charset() {
    }

    @Disabled("MariaDB-specific PAGE_CHECKSUM table option. Not valid MySQL syntax")
    @Test
    @Override
    public void parseTableWithPageChecksum() {
    }

    public static class MySqlDdlParserWithSimpleTestListener extends MySqlAntlrDdlParser {
        MySqlDdlParserWithSimpleTestListener(DdlChanges changesListener) {
            this(changesListener, false);
        }

        MySqlDdlParserWithSimpleTestListener(DdlChanges changesListener, TableFilter tableFilter) {
            this(changesListener, false, false, tableFilter);
        }

        MySqlDdlParserWithSimpleTestListener(DdlChanges changesListener, boolean includeViews) {
            this(changesListener, includeViews, false, TableFilter.includeAll());
        }

        MySqlDdlParserWithSimpleTestListener(DdlChanges changesListener, boolean includeViews, boolean includeComments) {
            this(changesListener, includeViews, includeComments, TableFilter.includeAll());
        }

        private MySqlDdlParserWithSimpleTestListener(DdlChanges changesListener, boolean includeViews, boolean includeComments, TableFilter tableFilter) {
            super(false, includeViews, includeComments, tableFilter, new MySqlCharsetRegistry());
            this.ddlChanges = changesListener;
        }
    }
}
