package flyway.pti3d;

import fi.nls.oskari.domain.map.view.Bundle;
import fi.nls.oskari.util.JSONHelper;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.oskari.helpers.AppSetupHelper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class V1_8__setup_dimension_change_to_2d extends BaseJavaMigration {

    private static final String BUNDLE_NAME = "dimension-change";
    private static final String APPLICATION_2D_NAME = "geoportal";
    private static final String APPLICATION_3D_NAME = "geoportal-3D";

    public void migrate(Context context) throws SQLException {
        Connection connection = context.getConnection();
        String uuid = AppSetupHelper.getUuidForDefaultSetup(connection, APPLICATION_2D_NAME);
        final List<Long> viewIds = AppSetupHelper.getSetupsForUserAndDefaultType(connection, APPLICATION_3D_NAME);

        for (Long id : viewIds) {
            if (!AppSetupHelper.appContainsBundle(connection, id, BUNDLE_NAME)) {
                AppSetupHelper.addBundleToApp(connection, id, BUNDLE_NAME);
            }
            Bundle bundle = AppSetupHelper.getAppBundle(connection, id, BUNDLE_NAME);
            bundle.setConfig(JSONHelper.createJSONObject("uuid", uuid).toString());
            AppSetupHelper.updateAppBundle(connection, id, bundle);
        }
    }
}
