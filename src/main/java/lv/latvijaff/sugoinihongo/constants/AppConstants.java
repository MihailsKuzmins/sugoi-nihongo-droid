package lv.latvijaff.sugoinihongo.constants;

import java.time.LocalDateTime;

public final class AppConstants {

	public static final class Models {

		public static final String EMPTY_ID = null;
		public static final int GRAMMAR_RULE_LIST_ITEM_BODY_MAX_LENGTH = 25;
		public static final int MIN_MARK = 0;
		public static final int MAX_MARK = 10;
		public static final int EXCELLENT_WORD_STATUS = 9;
		public static final int GOOD_WORD_STATUS = 7;
		public static final int AVERAGE_WORD_STATUS = 4;

		public static final LocalDateTime MIN_DATE_TIME = LocalDateTime.of(1930, 1, 1, 0, 0, 0);
	}

	public static final class System {

		public static final int MIN_QUIZ_ITEMS_COUNT = 4;
		public static final int MAX_QUIZ_ITEMS_COUNT = 6;
		public static final int MAX_STUDY_ITEMS_COUNT = 30;
		public static final int MIN_UNUSED_WORD_DAYS = 5;
		public static final int MAX_UNUSED_WORD_DAYS = 30;
		public static final int MAX_LOCAL_BACKUP_FILES_COUNT = 25;
		public static final int MIN_BACKUP_CREATION_FREQUENCY = 1;
		public static final int MAX_BACKUP_CREATION_FREQUENCY = 7;

		public static final int DEFAULT_DEBOUNCE_MS = 500;
		public static final int FOCUSABLE_DEBOUNCE_MS = 100;

		public static final String TRANSLATION_TRANSCRIPTION_SEPARATOR = java.lang.System.lineSeparator();
	}

	public static final class SharedPrefs {

		public static final class Keys {

			public static final String IS_FIRST_RUN = "isFirstRun";
			public static final String LAST_RUN_PROD_EPOCH = "lastRunProdEpoch";
			public static final String LAST_RUN_TEST_EPOCH = "lastRunTestEpoch";
			public static final String LAST_BACKUP_EPOCH = "lastBackupEpoch";
			public static final String STUDY_ITEMS_COUNT = "studyItemsCount";
			public static final String MIN_QUIZ_ITEMS_COUNT = "minQuizItemsCount";
			public static final String MAX_QUIZ_ITEMS_COUNT = "maxQuizItemsCount";
			public static final String UNUSED_WORD_DAYS = "unusedWordDays";
			public static final String IS_TRANSCRIPTION_SHOWN = "isTranscriptionShown";
			public static final String IS_TEST_DB_IN_USE = "isTestDbInUse";
			public static final String IS_ENTRY_ID_SHOWN = "isEntryIdShown";
			public static final String LOCAL_BACKUP_FILES_COUNT = "localBackupFilesCount";
			public static final String BACKUP_CREATION_FREQUENCY = "backupCreationFrequency";
		}
	}

	public static final class Keys {

		public static final class Navigation {

			public static final String SETTINGS_STUDY = "settingsStudy";
			public static final String SETTINGS_TECH = "settingsTech";
			public static final String SETTINGS_ABOUT = "settingsAbout";
			public static final String QUIZ_RESULTS = "quizResults";
		}

		public static final class Params {

			public static final String DOCUMENT_ID = "documentId";
			public static final String ENGLISH_TO_JAPANESE = "english2japanese";
		}
	}

	public static final class Firestore {

		public static final class Dirs {

			public static final String BACKUPS = "backups";
		}

		public static final class Collections {

			public static final String GRAMMAR_RULES = "grammarRules";
			public static final String SENTENCES = "sentences";
			public static final String WORDS = "words";
			public static final String WORDS_SECONDARY_PROPERTIES = "wordsSecondaryProperties";
		}

		public static final class CollectionsTest {

			public static final String GRAMMAR_RULES = "grammarRules_test";
			public static final String SENTENCES = "sentences_test";
			public static final String WORDS = "words_test";
			public static final String WORDS_SECONDARY_PROPERTIES = "wordsSecondaryProperties_test";
		}

		public static final class Keys {

			public static final class GrammarRules {

				public static final String BODY = "body";
				public static final String DATE_CREATED = "dateCreated";
				public static final String HEADER = "header";
			}

			public static final class Sentences {

				public static final String DATE_CREATED = "dateCreated";
				public static final String ENGLISH = "english";
				public static final String TRANSCRIPTION = "transcription";
				public static final String TRANSLATION = "translation";
			}

			public static final class Words {

				public static final String DATE_CREATED = "dateCreated";
				public static final String ENGLISH = "english";
				public static final String NOTES = "notes";
				public static final String TRANSCRIPTION = "transcription";
				public static final String TRANSLATION = "translation";
			}

			public static final class WordsSecondaryProps {

				public static final String DATE_LAST_ACCESSED = "dateLastAccessed";
				public static final String IS_FAVOURITE = "isFavourite";
				public static final String IS_STUDIABLE = "isStudiable";
				public static final String MARK = "mark";
				public static final String WORD_ID = "wordId";
			}
		}
	}
}
