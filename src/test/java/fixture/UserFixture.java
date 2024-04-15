package fixture;

import github.jhkoder.commerce.common.entity.Gender;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.domain.ItemProduct;
import github.jhkoder.commerce.user.domain.Role;
import github.jhkoder.commerce.user.domain.User;

public class UserFixture {

    public static User getFixtureUser() {
        return  new User("testuser", "testname","password12","test@gmail.com","01012341234", Gender.MAN, Role.USER);
    }
}
