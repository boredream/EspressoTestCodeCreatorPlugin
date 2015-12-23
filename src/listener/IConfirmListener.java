package listener;

import entity.EspressoAction;
import entity.EspressoAssertion;

import java.util.List;

public interface IConfirmListener {

    void onConfirm(List<EspressoAction> actions, List<EspressoAssertion> assertions);
}
