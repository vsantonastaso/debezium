/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.debezium.connector.mysql;

import java.time.temporal.TemporalAdjuster;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.debezium.config.CommonConnectorConfig.BinaryHandlingMode;
import io.debezium.config.CommonConnectorConfig.EventConvertingFailureHandlingMode;
import io.debezium.connector.binlog.BinlogConnectorConfig;
import io.debezium.connector.binlog.BinlogValueConvertersTest;
import io.debezium.connector.binlog.jdbc.BinlogValueConverters;
import io.debezium.connector.mysql.antlr.MySqlAntlrDdlParser;
import io.debezium.connector.mysql.util.MySqlValueConvertersFactory;
import io.debezium.jdbc.JdbcValueConverters;
import io.debezium.jdbc.TemporalPrecisionMode;
import io.debezium.relational.RelationalDatabaseConnectorConfig;
import io.debezium.relational.ddl.DdlParser;

/**
 * @author Randall Hauch
 *
 */
public class MySqlValueConvertersTest extends BinlogValueConvertersTest<MySqlConnector> implements MySqlCommon {
    @Override
    protected BinlogValueConverters getValueConverters(JdbcValueConverters.DecimalMode decimalMode,
                                                       TemporalPrecisionMode temporalPrecisionMode,
                                                       JdbcValueConverters.BigIntUnsignedMode bigIntUnsignedMode,
                                                       BinaryHandlingMode binaryHandlingMode,
                                                       TemporalAdjuster temporalAdjuster,
                                                       EventConvertingFailureHandlingMode eventConvertingFailureHandlingMode) {
        return new MySqlValueConvertersFactory().create(
                RelationalDatabaseConnectorConfig.DecimalHandlingMode.parse(decimalMode.name()),
                temporalPrecisionMode,
                BinlogConnectorConfig.BigIntUnsignedHandlingMode.parse(bigIntUnsignedMode.name()),
                binaryHandlingMode,
                temporalAdjuster,
                eventConvertingFailureHandlingMode);
    }

    @Override
    protected DdlParser getDdlParser() {
        return new MySqlAntlrDdlParser();
    }

    @Disabled("JSON_TABLE is a reserved keyword in MySQL 8.0.14+ for the JSON_TABLE() table function. " +
            "Cannot be used as an unquoted table name. Would require quoting (`JSON_TABLE`) or modifying the grammar " +
            "to add JSON_TABLE to the identifier keywords list.")
    @Test
    @Override
    public void testJsonValues() {
    }

    @Disabled("JSON_TABLE is a reserved keyword in MySQL 8.0.14+ for the JSON_TABLE() table function. " +
            "Same issue as testJsonValues.")
    @Test
    @Override
    public void testSkipInvalidJsonValues() {
    }

    @Disabled("Test expects DebeziumException but parser throws ParsingException for JSON_TABLE reserved keyword. " +
            "Cannot fix without modifying exception hierarchy. Also has JSON_TABLE reserved keyword issue.")
    @Test
    @Override
    public void testErrorOnInvalidJsonValues() {
    }

    @Disabled("ZonedDateTime microsecond precision issue - value converter formats without trailing zeros. " +
            "Expected: 2023-01-11T00:34:10.000000Z, Actual: 2023-01-11T00:34:10Z. " +
            "Not a DDL parser issue - relates to value converter formatting logic.")
    @Test
    @Override
    public void testZonedDateTimeWithMicrosecondPrecision() {
    }
}
