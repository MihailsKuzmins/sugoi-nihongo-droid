package lv.latvijaff.sugoinihongo.utils.helpers;

import com.google.firebase.firestore.ListenerRegistration;

import lv.latvijaff.sugoinihongo.utils.ObjectUtils;

public class WordAndSecondaryPropsListenerRegistration {

	private ListenerRegistration mWordListenerRegistration;
	private ListenerRegistration mSecondaryPropsListenerRegistration;

	public void setWordListenerRegistration(ListenerRegistration mWordListenerRegistration) {
		this.mWordListenerRegistration = mWordListenerRegistration;
	}

	public void setSecondaryPropsListenerRegistration(ListenerRegistration mSecondaryPropsListenerRegistration) {
		this.mSecondaryPropsListenerRegistration = mSecondaryPropsListenerRegistration;
	}

	public void remove() {
		ObjectUtils.invokeIfNotNull(mWordListenerRegistration, ListenerRegistration::remove);
		ObjectUtils.invokeIfNotNull(mSecondaryPropsListenerRegistration, ListenerRegistration::remove);
	}
}
