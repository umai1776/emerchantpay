package com.transactions.payment.services;

import com.transactions.payment.dto.UpdatePasswordDto;
import com.transactions.payment.dto.UserDto;
import com.transactions.payment.model.ERole;
import com.transactions.payment.model.Role;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserServicesTest {

    @MockBean
    private UserServices userServices;

    @Mock
    private UserDto user1;
    @Mock
    private UserDto user2;
    @Mock
    private UpdatePasswordDto passwordDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        Set<Role> role = Stream.of(new Role(ERole.ROLE_ADMIN))
                .collect(Collectors.toCollection(HashSet::new));
        this.user1  = new UserDto(1L,"admin","u.beijlerbeijli@jrevolt.nl","$2a$10$ishPp2Me2Cw7FHMh.5UrMuePICMPxKEPG4ppoks.BQRcUrroSX5JG","$2a$10$ishPp2Me2Cw7FHMh.5UrMuePICMPxKEPG4ppoks.BQRcUrroSX5JG", true, role);
        this.user2  = new UserDto(2L,"admin2","ubey1982@gmail.com","PWPFRjK9skEfpaAY0Z2SRWWcmHZycjwjubRokpN-K4i0_b4","PWPFRjK9skEfpaAY0Z2SRWWcmHZycjwjubRokpN-K4i0_b4", true, role);
        doReturn(Lists.newArrayList(user1, user2)).when(userServices).showAllUsers();
        doReturn(user1).when(userServices).findById(1L);
        when(userServices.findById(1L)).thenReturn(this.user1);
        doNothing().when(userServices).deleteUser(1L);
    }


    @Test
    public void shoulReturnListOfUsers() throws Exception{
        List<UserDto> usersList = userServices.showAllUsers();
        assertEquals(2, usersList.size());

        verify(userServices, times(1)).showAllUsers();
    }

    @DisplayName("Test if user is created")
    @Test
    public void shouldCreateNewUser(){
        userServices.createUser(user1);
        verify(userServices, times(1)).createUser(user1);
    }

    @DisplayName("find user by ID")
    @Test
    public void shouldFindUserById() throws Exception {
        assertThat(userServices.findById(user1.getId())).isEqualTo(user1);

        verify(userServices, times(1)).findById(1L);
    }

    @DisplayName("reset password by User")
    @Test
    public void shouldChangeUserPasswordByUser() {
        userServices.updateUserPassword(1l, passwordDto);
    }

    @AfterEach
    public void tearDown() {
        userServices.deleteUser(1L);

        verify(userServices, times(1)).deleteUser(1l);
    }
}
