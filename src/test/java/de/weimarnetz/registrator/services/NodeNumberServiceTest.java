package de.weimarnetz.registrator.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.assertj.core.util.Lists;
import org.assertj.core.util.Maps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import de.weimarnetz.MockitoTest;
import de.weimarnetz.registrator.configuration.NetworkInformation;
import de.weimarnetz.registrator.configuration.NetworksConfiguration;
import de.weimarnetz.registrator.exceptions.NetworkNotFoundException;
import de.weimarnetz.registrator.exceptions.NoMoreNodesException;
import de.weimarnetz.registrator.model.Node;
import de.weimarnetz.registrator.repository.RegistratorRepository;

public class NodeNumberServiceTest extends MockitoTest {

    @Mock
    private RegistratorRepository registratorRepository;
    @Mock
    private NetworksConfiguration networksConfiguration;

    @InjectMocks
    private NodeNumberService nodeNumberService;

    @BeforeEach
    public void setup() {
        NetworkInformation networkInformation = new NetworkInformation();
        networkInformation.setMinNodeNumber(2);
        networkInformation.setMaxNodeNumber(10);
        Map<String, NetworkInformation> networkInformationMap = Maps.newHashMap("testnet", networkInformation);
        when(networksConfiguration.getMap()).thenReturn(networkInformationMap);
    }

    @Test
    public void nextNodeNumberOnEmptyList() throws NoMoreNodesException, NetworkNotFoundException {
        // given
        when(registratorRepository.findAllByNetwork("testnet")).thenReturn(Lists.emptyList());

        // when
        int nextAvailableNodeNumber = nodeNumberService.getNextAvailableNodeNumber("testnet");

        // then
        assertThat(nextAvailableNodeNumber).isEqualTo(2);
    }

    @Test
    public void nextNodeNumberOnFilledList() throws NoMoreNodesException, NetworkNotFoundException {
        // given
        when(registratorRepository.findAllByNetwork("testnet")).thenReturn(Lists.newArrayList(getNodeList(2, 3, 4, 6)));

        // when
        int nextAvailableNodeNumber = nodeNumberService.getNextAvailableNodeNumber("testnet");

        // then
        assertThat(nextAvailableNodeNumber).isEqualTo(5);
    }

    @Test
    public void firstNodeNumberOnFilledList() throws NoMoreNodesException, NetworkNotFoundException {
        // given
        when(registratorRepository.findAllByNetwork("testnet")).thenReturn(Lists.newArrayList(getNodeList(3, 4, 6)));

        // when
        int nextAvailableNodeNumber = nodeNumberService.getNextAvailableNodeNumber("testnet");

        // then
        assertThat(nextAvailableNodeNumber).isEqualTo(2);
    }

    @Test
    public void nextNodeNumberOnFilledListLeftHalf() throws NoMoreNodesException, NetworkNotFoundException {
        // given
        when(registratorRepository.findAllByNetwork("testnet")).thenReturn(Lists.newArrayList(getNodeList(2, 4, 5, 6, 7, 8, 9, 10)));

        // when
        int nextAvailableNodeNumber = nodeNumberService.getNextAvailableNodeNumber("testnet");

        // then
        assertThat(nextAvailableNodeNumber).isEqualTo(3);
    }

    @Test
    public void nextNodeNumberOnFilledListMiddle() throws NoMoreNodesException, NetworkNotFoundException {
        // given
        when(registratorRepository.findAllByNetwork("testnet")).thenReturn(Lists.newArrayList(getNodeList(2, 3, 4, 5, 7, 8, 9, 10)));

        // when
        int nextAvailableNodeNumber = nodeNumberService.getNextAvailableNodeNumber("testnet");

        // then
        assertThat(nextAvailableNodeNumber).isEqualTo(6);
    }


    @Test
    public void lastNodeNumberOnFilledList() throws NoMoreNodesException, NetworkNotFoundException {
        // given
        when(registratorRepository.findAllByNetwork("testnet")).thenReturn(Lists.newArrayList(getNodeList(2, 3, 4, 5, 8, 6, 7, 9)));

        // when
        int nextAvailableNodeNumber = nodeNumberService.getNextAvailableNodeNumber("testnet");

        // then
        assertThat(nextAvailableNodeNumber).isEqualTo(10);
    }

    @Test
    public void noMoreNewNodesOnFilledList() throws NetworkNotFoundException {
        // given
        when(registratorRepository.findAllByNetwork("testnet")).thenReturn(Lists.newArrayList(getNodeList(2, 3, 4, 5, 8, 6, 7, 9, 10)));

        // when
        assertThrows(NoMoreNodesException.class, () -> nodeNumberService.getNextAvailableNodeNumber("testnet"));
    }

    @Test
    public void netWorkNotFound() throws NetworkNotFoundException {
        // given
        when(registratorRepository.findAllByNetwork("not_my_network")).thenReturn(Lists.newArrayList(getNodeList(2, 3, 4, 5, 8, 6, 7, 9, 10)));

        // when
        assertThrows(NetworkNotFoundException.class, () -> nodeNumberService.getNextAvailableNodeNumber("not_my_network"));
    }


    private List<Node> getNodeList(int... nodeNumbers) {
        List<Node> nodeList = Lists.newArrayList();
        for (int nodeNumber : nodeNumbers) {
            nodeList.add(Node.builder().number(nodeNumber).build());
        }
        return nodeList;
    }

    @Test
    public void nodeNumberToSmall() throws NetworkNotFoundException {
        // when
        boolean nodeNumberValid = nodeNumberService.isNodeNumberValid(1, "testnet");

        // then
        assertThat(nodeNumberValid).isFalse();
    }

    @Test
    public void nodeNumberToBig() throws NetworkNotFoundException {
        // when
        boolean nodeNumberValid = nodeNumberService.isNodeNumberValid(11, "testnet");

        // then
        assertThat(nodeNumberValid).isFalse();
    }

    @Test
    public void nodeNumberInRange() throws NetworkNotFoundException {
        // when
        boolean nodeNumberValid = nodeNumberService.isNodeNumberValid(5, "testnet");

        // then
        assertThat(nodeNumberValid).isTrue();
    }

}