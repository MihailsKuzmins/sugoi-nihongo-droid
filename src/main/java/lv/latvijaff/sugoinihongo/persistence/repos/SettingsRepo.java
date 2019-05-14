package lv.latvijaff.sugoinihongo.persistence.repos;

import android.content.Context;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import com.google.common.collect.Ordering;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import lv.latvijaff.sugoinihongo.BuildConfig;
import lv.latvijaff.sugoinihongo.R;
import lv.latvijaff.sugoinihongo.constants.AppConstants.Keys.Navigation;
import lv.latvijaff.sugoinihongo.persistence.models.SettingModel;
import lv.latvijaff.sugoinihongo.persistence.models.ThirdPartyLibraryModel;

public class SettingsRepo {

	public List<SettingModel> createSettingsList() {
		List<SettingModel> list = new ArrayList<>();
		list.add(createSettingModel(Navigation.SETTINGS_STUDY, R.drawable.ic_assignment_black_32dp, R.string.general_study, R.string.settings_study_description));
		list.add(createSettingModel(Navigation.SETTINGS_TECH, R.drawable.ic_build_black_32dp, R.string.settings_tech_title, R.string.settings_tech_description));
		list.add(createSettingModel(Navigation.SETTINGS_ABOUT, R.drawable.ic_info_outline_black_32dp, R.string.settings_about_title, R.string.general_version, BuildConfig.VERSION_NAME));

		return list;
	}

	public List<ThirdPartyLibraryModel> createThirdPartyLibrariesList(Context context) {
		try (InputStream stream = context.getAssets().open("third_party_libs.json");
			 InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {

			Type type = new TypeToken<ArrayList<ThirdPartyLibraryModel>>(){}.getType();
			List<ThirdPartyLibraryModel> list = new Gson().fromJson(reader, type);

			return Ordering.natural()
				.onResultOf(ThirdPartyLibraryModel::getName)
				.sortedCopy(list);

		} catch (IOException e) {
			e.printStackTrace();
		}

		// Function open() does not allow to throw exceptions, therefore, return an empty list
		return new ArrayList<>();
	}

	private SettingModel createSettingModel(String id, @DrawableRes int image, @StringRes int title, @StringRes int description) {
		return createSettingModel(id, image, title, description, null);
	}

	private SettingModel createSettingModel(String id, @DrawableRes int image, @StringRes int title, @StringRes int description, String descriptionString) {
		SettingModel model = new SettingModel(id);
		model.setImage(image);
		model.setTitle(title);
		model.setDescription(description);
		model.setDescriptionString(descriptionString);

		return model;
	}
}
