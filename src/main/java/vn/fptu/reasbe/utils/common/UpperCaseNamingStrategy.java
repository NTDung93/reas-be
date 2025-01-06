package vn.fptu.reasbe.utils.common;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

/**
 *
 * @author ntig
 */
public class UpperCaseNamingStrategy extends PhysicalNamingStrategyStandardImpl {

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        // Convert the table name to uppercase
        return new Identifier(name.getText().toUpperCase(), true);
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
        // Convert the column name to uppercase
        return new Identifier(name.getText().toUpperCase(), true);
    }
}