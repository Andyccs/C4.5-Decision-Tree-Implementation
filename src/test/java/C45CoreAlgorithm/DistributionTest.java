package C45CoreAlgorithm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import DataDefination.Attribute;
import DataDefination.Instance;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * TODO(andyccs): comments
 */
public class DistributionTest {

  private static final String TARGET_NAME = "fruit";
  private static final String TARGET_VALUE_APPLE = "apple";
  private static final String TARGET_VALUE_ORANGE = "orange";
  private static final String TARGET_VALUE_GRAPE = "grape";
  private static final String TARGET_VALUES =
      "{" + TARGET_VALUE_APPLE + "," + TARGET_VALUE_ORANGE + "," + TARGET_VALUE_GRAPE + "}";

  private Attribute targetAttributeFake;
  private List<Instance> instancesFake;

  @Before
  public void before() throws IOException {
    targetAttributeFake = new Attribute(TARGET_NAME, TARGET_VALUES);

    Instance instance1 = new Instance();
    instance1.addAttribute(TARGET_NAME, TARGET_VALUE_APPLE);

    Instance instance2 = new Instance();
    instance2.addAttribute(TARGET_NAME, TARGET_VALUE_APPLE);

    Instance instance3 = new Instance();
    instance3.addAttribute(TARGET_NAME, TARGET_VALUE_ORANGE);

    Instance instance4 = new Instance();
    instance4.addAttribute(TARGET_NAME, TARGET_VALUE_GRAPE);

    instancesFake = new ArrayList<>();
    instancesFake.add(instance1);
    instancesFake.add(instance2);
    instancesFake.add(instance3);
    instancesFake.add(instance4);
  }

  @After
  public void after() {
    targetAttributeFake = null;
    instancesFake = null;
  }

  @Test
  public void testInitialization() {
    Distribution distribution = new Distribution(targetAttributeFake, 2);
    assertNotNull(distribution);
    assertEquals(distribution.getTargetAttribute(), targetAttributeFake);
    assertNotNull(distribution.getFrequencyByTargetValue());
    assertNotNull(distribution.getFrequencyByBagIndexByTargetValue(0));
    assertNotNull(distribution.getFrequencyByBagIndexByTargetValue(1));
    assertEquals(distribution.getTotalFrequency(), 0, 0);
    assertEquals(distribution.getNumberOfBag(), 2, 0);
  }

  @Test
  public void testAdd() {
    Distribution distribution = new Distribution(targetAttributeFake, 2);
    distribution.add(0, instancesFake);

    assertEquals(distribution.getTotalFrequency(), 4, 0);

    Hashtable<String, Double> frequencyByClassValue = distribution.getFrequencyByTargetValue();
    assertEquals(frequencyByClassValue.get(TARGET_VALUE_APPLE), 2, 0);
    assertEquals(frequencyByClassValue.get(TARGET_VALUE_ORANGE), 1, 0);
    assertEquals(frequencyByClassValue.get(TARGET_VALUE_GRAPE), 1, 0);

    Hashtable<String, Double> frequencyByBagIndex0ByClassValue = distribution
        .getFrequencyByBagIndexByTargetValue(0);
    assertEquals(frequencyByBagIndex0ByClassValue.get(TARGET_VALUE_APPLE), 2, 0);
    assertEquals(frequencyByBagIndex0ByClassValue.get(TARGET_VALUE_ORANGE), 1, 0);
    assertEquals(frequencyByBagIndex0ByClassValue.get(TARGET_VALUE_GRAPE), 1, 0);

    Hashtable<String, Double> frequencyByBagIndex1ByClassValue = distribution
        .getFrequencyByBagIndexByTargetValue(1);
    assertEquals(frequencyByBagIndex1ByClassValue.get(TARGET_VALUE_APPLE), 0, 0);
    assertEquals(frequencyByBagIndex1ByClassValue.get(TARGET_VALUE_ORANGE), 0, 0);
    assertEquals(frequencyByBagIndex1ByClassValue.get(TARGET_VALUE_GRAPE), 0, 0);

    Instance instance = new Instance();
    instance.addAttribute(TARGET_NAME, TARGET_VALUE_GRAPE);

    ArrayList<Instance> additionalInstancesFake = new ArrayList<>();
    additionalInstancesFake.add(instance);

    distribution.add(1, additionalInstancesFake);

    assertEquals(distribution.getTotalFrequency(), 5, 0);

    Hashtable<String, Double> frequencyByClassValueAfter = distribution.getFrequencyByTargetValue();
    assertEquals(2, frequencyByClassValueAfter.get(TARGET_VALUE_APPLE), 0);
    assertEquals(1, frequencyByClassValueAfter.get(TARGET_VALUE_ORANGE), 0);
    assertEquals(2, frequencyByClassValueAfter.get(TARGET_VALUE_GRAPE), 0);

    Hashtable<String, Double> frequencyByBagIndex0ByClassValueAfter = distribution
        .getFrequencyByBagIndexByTargetValue(0);
    assertEquals(2, frequencyByBagIndex0ByClassValueAfter.get(TARGET_VALUE_APPLE), 0);
    assertEquals(1, frequencyByBagIndex0ByClassValueAfter.get(TARGET_VALUE_ORANGE), 0);
    assertEquals(1, frequencyByBagIndex0ByClassValueAfter.get(TARGET_VALUE_GRAPE), 0);

    Hashtable<String, Double> frequencyByBagIndex1ByClassValueAfter = distribution
        .getFrequencyByBagIndexByTargetValue(1);
    assertEquals(0, frequencyByBagIndex1ByClassValueAfter.get(TARGET_VALUE_APPLE), 0);
    assertEquals(0, frequencyByBagIndex1ByClassValueAfter.get(TARGET_VALUE_ORANGE), 0);
    assertEquals(1, frequencyByBagIndex1ByClassValueAfter.get(TARGET_VALUE_GRAPE), 0);
  }

  @Test
  public void testShift() {
    Distribution distribution = new Distribution(targetAttributeFake, 2);
    distribution.add(0, instancesFake);

    // Move the first apple to bag 1.
    distribution.shift(0, 1, instancesFake, 0, 0);

    assertEquals(distribution.getTotalFrequency(), 4, 0);

    Hashtable<String, Double> frequencyByClassValue = distribution.getFrequencyByTargetValue();
    assertEquals(2, frequencyByClassValue.get(TARGET_VALUE_APPLE), 0);
    assertEquals(1, frequencyByClassValue.get(TARGET_VALUE_ORANGE), 0);
    assertEquals(1, frequencyByClassValue.get(TARGET_VALUE_GRAPE), 0);

    Hashtable<String, Double> frequencyByBagIndex0ByClassValue = distribution
        .getFrequencyByBagIndexByTargetValue(0);
    assertEquals(1, frequencyByBagIndex0ByClassValue.get(TARGET_VALUE_APPLE), 0);
    assertEquals(1, frequencyByBagIndex0ByClassValue.get(TARGET_VALUE_ORANGE), 0);
    assertEquals(1, frequencyByBagIndex0ByClassValue.get(TARGET_VALUE_GRAPE), 0);

    Hashtable<String, Double> frequencyByBagIndex1ByClassValue = distribution
        .getFrequencyByBagIndexByTargetValue(1);
    assertEquals(1, frequencyByBagIndex1ByClassValue.get(TARGET_VALUE_APPLE), 0);
    assertEquals(0, frequencyByBagIndex1ByClassValue.get(TARGET_VALUE_ORANGE), 0);
    assertEquals(0, frequencyByBagIndex1ByClassValue.get(TARGET_VALUE_GRAPE), 0);

    distribution.shift(0, 1, instancesFake, 0, 0);

    assertEquals(distribution.getTotalFrequency(), 4, 0);

    Hashtable<String, Double> frequencyByClassValueAfter = distribution.getFrequencyByTargetValue();
    assertEquals(2, frequencyByClassValueAfter.get(TARGET_VALUE_APPLE), 0);
    assertEquals(1, frequencyByClassValueAfter.get(TARGET_VALUE_ORANGE), 0);
    assertEquals(1, frequencyByClassValueAfter.get(TARGET_VALUE_GRAPE), 0);

    Hashtable<String, Double> frequencyByBagIndex0ByClassValueAfter = distribution
        .getFrequencyByBagIndexByTargetValue(0);
    assertEquals(0, frequencyByBagIndex0ByClassValueAfter.get(TARGET_VALUE_APPLE), 0);
    assertEquals(1, frequencyByBagIndex0ByClassValueAfter.get(TARGET_VALUE_ORANGE), 0);
    assertEquals(1, frequencyByBagIndex0ByClassValueAfter.get(TARGET_VALUE_GRAPE), 0);

    Hashtable<String, Double> frequencyByBagIndex1ByClassValueAfter = distribution
        .getFrequencyByBagIndexByTargetValue(1);
    assertEquals(2, frequencyByBagIndex1ByClassValueAfter.get(TARGET_VALUE_APPLE), 0);
    assertEquals(0, frequencyByBagIndex1ByClassValueAfter.get(TARGET_VALUE_ORANGE), 0);
    assertEquals(0, frequencyByBagIndex1ByClassValueAfter.get(TARGET_VALUE_GRAPE), 0);
  }
}
