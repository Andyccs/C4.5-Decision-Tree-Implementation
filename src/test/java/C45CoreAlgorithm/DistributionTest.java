package C45CoreAlgorithm;

import DataDefination.Attribute;
import DataDefination.Instance;
import java.util.Hashtable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
  private List<Instance> instances;

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

    instances = new ArrayList<>();
    instances.add(instance1);
    instances.add(instance2);
    instances.add(instance3);
    instances.add(instance4);
  }

  @After
  public void after() {
    targetAttributeFake = null;
    instances = null;
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
    distribution.add(0, instances);

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

    ArrayList<Instance> additionalInstances = new ArrayList<>();
    additionalInstances.add(instance);

    distribution.add(1, additionalInstances);

    assertEquals(distribution.getTotalFrequency(), 5, 0);

    Hashtable<String, Double> frequencyByClassValueAfter = distribution.getFrequencyByTargetValue();
    assertEquals(frequencyByClassValueAfter.get(TARGET_VALUE_APPLE), 2, 0);
    assertEquals(frequencyByClassValueAfter.get(TARGET_VALUE_ORANGE), 1, 0);
    assertEquals(frequencyByClassValueAfter.get(TARGET_VALUE_GRAPE), 2, 0);

    Hashtable<String, Double> frequencyByBagIndex0ByClassValueAfter = distribution
        .getFrequencyByBagIndexByTargetValue(0);
    assertEquals(frequencyByBagIndex0ByClassValueAfter.get(TARGET_VALUE_APPLE), 2, 0);
    assertEquals(frequencyByBagIndex0ByClassValueAfter.get(TARGET_VALUE_ORANGE), 1, 0);
    assertEquals(frequencyByBagIndex0ByClassValueAfter.get(TARGET_VALUE_GRAPE), 1, 0);

    Hashtable<String, Double> frequencyByBagIndex1ByClassValueAfter = distribution
        .getFrequencyByBagIndexByTargetValue(1);
    assertEquals(frequencyByBagIndex1ByClassValueAfter.get(TARGET_VALUE_APPLE), 0, 0);
    assertEquals(frequencyByBagIndex1ByClassValueAfter.get(TARGET_VALUE_ORANGE), 0, 0);
    assertEquals(frequencyByBagIndex1ByClassValueAfter.get(TARGET_VALUE_GRAPE), 1, 0);
  }
}
