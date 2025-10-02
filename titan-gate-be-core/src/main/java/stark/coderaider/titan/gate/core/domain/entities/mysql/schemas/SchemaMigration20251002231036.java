package stark.coderaider.titan.gate.core.domain.entities.mysql.schemas;

import stark.coderaider.fluentschema.commons.schemas.ColumnMetadata;
import stark.coderaider.fluentschema.commons.schemas.KeyMetadata;
import stark.coderaider.fluentschema.commons.schemas.SchemaMigrationBase;
import java.util.List;

public class SchemaMigration20251002231036 extends SchemaMigrationBase {
    @Override
    public void forward() {
        setInitialized(false);
        forwardBuilder.createTable("user_role", builder -> {
            builder.column().name("user_id").type("BIGINT").nullable(false).unique(true).comment("User ID.");
            builder.column().name("role_ids").type("VARCHAR(500)").nullable(false).unique(false).comment("Role IDs.");
            builder.column().name("id").type("BIGINT").nullable(false).unique(false).autoIncrement(1);
            builder.column().name("creator_id").type("BIGINT").nullable(false).unique(false);
            builder.column().name("creation_time").type("DATETIME").nullable(true).unique(false).defaultValue("NOW()");
            builder.column().name("modifier_id").type("BIGINT").nullable(false).unique(false);
            builder.column().name("modification_time").type("DATETIME").nullable(true).unique(false)
                    .defaultValue("NOW()").onUpdate("NOW()");
            builder.primaryKey().columnName("id");
            builder.engine("InnoDB");
        });
        forwardBuilder.createTable("role", builder -> {
            builder.column().name("code").type("VARCHAR(50)").nullable(false).unique(true).comment("角色标识");
            builder.column().name("name").type("VARCHAR(50)").nullable(false).unique(false).comment("角色名称");
            builder.column().name("description").type("VARCHAR(255)").nullable(true).unique(false).comment("描述");
            builder.column().name("id").type("BIGINT").nullable(false).unique(false).autoIncrement(1);
            builder.column().name("creator_id").type("BIGINT").nullable(false).unique(false);
            builder.column().name("creation_time").type("DATETIME").nullable(true).unique(false).defaultValue("NOW()");
            builder.column().name("modifier_id").type("BIGINT").nullable(false).unique(false);
            builder.column().name("modification_time").type("DATETIME").nullable(true).unique(false)
                    .defaultValue("NOW()").onUpdate("NOW()");
            builder.primaryKey().columnName("id");
            builder.engine("InnoDB");
        });
        forwardBuilder.createTable("login_state_token", builder -> {
            builder.column().name("user_id").type("BIGINT").nullable(false).unique(false).comment("User ID.");
            builder.column().name("token").type("VARCHAR(500)").nullable(false).unique(false).comment("The JWT Token.");
            builder.column().name("expiration_time").type("DATETIME").nullable(false).unique(false)
                    .comment("Expiration time.");
            builder.column().name("valid").type("BOOL").nullable(false).unique(false)
                    .comment("Whether the token is valid.");
            builder.column().name("id").type("BIGINT").nullable(false).unique(false).autoIncrement(1);
            builder.column().name("creator_id").type("BIGINT").nullable(false).unique(false);
            builder.column().name("creation_time").type("DATETIME").nullable(true).unique(false).defaultValue("NOW()");
            builder.column().name("modifier_id").type("BIGINT").nullable(false).unique(false);
            builder.column().name("modification_time").type("DATETIME").nullable(true).unique(false)
                    .defaultValue("NOW()").onUpdate("NOW()");
            builder.primaryKey().columnName("id");
            builder.engine("InnoDB");
            builder.comment("Tokens of login states.");
        });
        forwardBuilder.createTable("user", builder -> {
            builder.column().name("username").type("VARCHAR(64)").nullable(false).unique(true).comment("Login name.");
            builder.column().name("encrypted_password").type("VARCHAR(128)").nullable(false).unique(false)
                    .comment("Encrypted password.");
            builder.column().name("state").type("INT").nullable(false).unique(false)
                    .comment("State of the account. 0 - Active; 1 - Locked; 2 - Deleted.");
            builder.column().name("email").type("VARCHAR(64)").nullable(false).unique(false)
                    .comment("Email address of the user.");
            builder.column().name("phone_number_country_code").type("VARCHAR(5)").nullable(false).unique(false)
                    .comment("Phone number country code.");
            builder.column().name("phone_number").type("VARCHAR(16)").nullable(false).unique(false)
                    .comment("Phone number.");
            builder.column().name("id").type("BIGINT").nullable(false).unique(false).autoIncrement(1);
            builder.column().name("creator_id").type("BIGINT").nullable(false).unique(false);
            builder.column().name("creation_time").type("DATETIME").nullable(true).unique(false).defaultValue("NOW()");
            builder.column().name("modifier_id").type("BIGINT").nullable(false).unique(false);
            builder.column().name("modification_time").type("DATETIME").nullable(true).unique(false)
                    .defaultValue("NOW()").onUpdate("NOW()");
            builder.primaryKey().columnName("id");
            builder.engine("InnoDB");
            builder.comment("Information that used for user login.");
        });
        forwardBuilder.createTable("user_oauth2", builder -> {
            builder.column().name("user_id").type("BIGINT").nullable(false).unique(true).comment("用户 ID");
            builder.column().name("provider").type("VARCHAR(64)").nullable(false).unique(false)
                    .comment("外部 OAuth2 提供方");
            builder.column().name("provider_uid").type("VARCHAR(64)").nullable(false).unique(false)
                    .comment("外部 OAuth2 提供方账号唯一标识");
            builder.column().name("access_token").type("VARCHAR(500)").nullable(true).unique(false)
                    .comment("Access token of the provider.");
            builder.column().name("refresh_token").type("VARCHAR(500)").nullable(true).unique(false)
                    .comment("Refresh token of the provider.");
            builder.column().name("expires_in").type("BIGINT").nullable(false).unique(false)
                    .comment("Access token expiration time in seconds.");
            builder.column().name("id").type("BIGINT").nullable(false).unique(false).autoIncrement(1);
            builder.column().name("creator_id").type("BIGINT").nullable(false).unique(false);
            builder.column().name("creation_time").type("DATETIME").nullable(true).unique(false).defaultValue("NOW()");
            builder.column().name("modifier_id").type("BIGINT").nullable(false).unique(false);
            builder.column().name("modification_time").type("DATETIME").nullable(true).unique(false)
                    .defaultValue("NOW()").onUpdate("NOW()");
            builder.primaryKey().columnName("id");
            builder.engine("InnoDB");
        });
    }
    @Override
    public void backward() {
        backwardBuilder.dropTable("user_role");
        backwardBuilder.dropTable("role");
        backwardBuilder.dropTable("login_state_token");
        backwardBuilder.dropTable("user");
        backwardBuilder.dropTable("user_oauth2");
    }
}