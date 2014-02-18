/**
 * 
 */
package net.blog.dev.gestion.stocks.back.migration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import net.blog.dev.gestion.stocks.back.KContext;

/**
 * @author Kiva
 * 
 */
@ApplicationScoped
public class MigrationEngine {

	@Inject
	@Any
	Instance<IMigrationVersion> migrations;

	public void init(@Observes @Migration KContext context) {
		String currentVersion = context.getConfiguration().getVersion();
		if (currentVersion == null)
			currentVersion = "1.0.0";
		final Iterator<IMigrationVersion> iterator = migrations.iterator();
		List<IMigrationVersion> migrationToExecute = new ArrayList<>();
		while (iterator.hasNext()) {
			final IMigrationVersion migration = iterator.next();
			if (versionIsHigher(currentVersion, migration.getVersion()))
				migrationToExecute.add(migration);
		}
		Collections.sort(migrationToExecute,
				new Comparator<IMigrationVersion>() {

					@Override
					public int compare(IMigrationVersion o1,
							IMigrationVersion o2) {
						if (o1 == null || o2 == null)
							return 0;
						if (o1.getVersion() == null || o2.getVersion() == null)
							return 0;
						if (versionIsHigher(o1.getVersion(), o2.getVersion()))
							return -1;
						if (versionIsHigher(o2.getVersion(), o1.getVersion()))
							return 1;
						return 0;
					}
				});
		for (IMigrationVersion migration : migrationToExecute) {
			migration.migrate(context);
			currentVersion = migration.getVersion();
		}
		if (!currentVersion.equals(context.getConfiguration().getVersion())) {
			context.getConfiguration().setVersion(currentVersion);
			context.saveConfiguration();
		}
	}

	/**
	 * Version > currentVersion ?
	 * 
	 * @param currentVersion
	 * @param version
	 * @return
	 */
	private boolean versionIsHigher(String currentVersion, String version) {
		if (currentVersion == null || version == null)
			return false;

		final String[] splitCurrentVersion = currentVersion.split("\\.");
		final String[] splitVersion = version.split("\\.");
		for (int i = 0; i < splitVersion.length; i++) {
			int number = Integer.parseInt(splitVersion[i]);
			// si le digit n'existe pas, alors la version est superieur a la
			// currentVersion
			if (splitCurrentVersion.length <= i)
				return true;
			int currentNumber = Integer.parseInt(splitCurrentVersion[i]);
			if (number > currentNumber)
				return true;
		}
		return false;
	}
}
