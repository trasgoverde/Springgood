import { element, by, ElementFinder } from 'protractor';

export class AppprofileComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-appprofile div table .btn-danger'));
  title = element.all(by.css('jhi-appprofile div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class AppprofileUpdatePage {
  pageTitle = element(by.id('jhi-appprofile-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  creationDateInput = element(by.id('field_creationDate'));
  genderSelect = element(by.id('field_gender'));
  phoneInput = element(by.id('field_phone'));
  bioInput = element(by.id('field_bio'));
  facebookInput = element(by.id('field_facebook'));
  twitterInput = element(by.id('field_twitter'));
  linkedinInput = element(by.id('field_linkedin'));
  instagramInput = element(by.id('field_instagram'));
  googlePlusInput = element(by.id('field_googlePlus'));
  birthdateInput = element(by.id('field_birthdate'));
  sibblingsInput = element(by.id('field_sibblings'));

  appuserSelect = element(by.id('field_appuser'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setCreationDateInput(creationDate: string): Promise<void> {
    await this.creationDateInput.sendKeys(creationDate);
  }

  async getCreationDateInput(): Promise<string> {
    return await this.creationDateInput.getAttribute('value');
  }

  async setGenderSelect(gender: string): Promise<void> {
    await this.genderSelect.sendKeys(gender);
  }

  async getGenderSelect(): Promise<string> {
    return await this.genderSelect.element(by.css('option:checked')).getText();
  }

  async genderSelectLastOption(): Promise<void> {
    await this.genderSelect.all(by.tagName('option')).last().click();
  }

  async setPhoneInput(phone: string): Promise<void> {
    await this.phoneInput.sendKeys(phone);
  }

  async getPhoneInput(): Promise<string> {
    return await this.phoneInput.getAttribute('value');
  }

  async setBioInput(bio: string): Promise<void> {
    await this.bioInput.sendKeys(bio);
  }

  async getBioInput(): Promise<string> {
    return await this.bioInput.getAttribute('value');
  }

  async setFacebookInput(facebook: string): Promise<void> {
    await this.facebookInput.sendKeys(facebook);
  }

  async getFacebookInput(): Promise<string> {
    return await this.facebookInput.getAttribute('value');
  }

  async setTwitterInput(twitter: string): Promise<void> {
    await this.twitterInput.sendKeys(twitter);
  }

  async getTwitterInput(): Promise<string> {
    return await this.twitterInput.getAttribute('value');
  }

  async setLinkedinInput(linkedin: string): Promise<void> {
    await this.linkedinInput.sendKeys(linkedin);
  }

  async getLinkedinInput(): Promise<string> {
    return await this.linkedinInput.getAttribute('value');
  }

  async setInstagramInput(instagram: string): Promise<void> {
    await this.instagramInput.sendKeys(instagram);
  }

  async getInstagramInput(): Promise<string> {
    return await this.instagramInput.getAttribute('value');
  }

  async setGooglePlusInput(googlePlus: string): Promise<void> {
    await this.googlePlusInput.sendKeys(googlePlus);
  }

  async getGooglePlusInput(): Promise<string> {
    return await this.googlePlusInput.getAttribute('value');
  }

  async setBirthdateInput(birthdate: string): Promise<void> {
    await this.birthdateInput.sendKeys(birthdate);
  }

  async getBirthdateInput(): Promise<string> {
    return await this.birthdateInput.getAttribute('value');
  }

  async setSibblingsInput(sibblings: string): Promise<void> {
    await this.sibblingsInput.sendKeys(sibblings);
  }

  async getSibblingsInput(): Promise<string> {
    return await this.sibblingsInput.getAttribute('value');
  }

  async appuserSelectLastOption(): Promise<void> {
    await this.appuserSelect.all(by.tagName('option')).last().click();
  }

  async appuserSelectOption(option: string): Promise<void> {
    await this.appuserSelect.sendKeys(option);
  }

  getAppuserSelect(): ElementFinder {
    return this.appuserSelect;
  }

  async getAppuserSelectedOption(): Promise<string> {
    return await this.appuserSelect.element(by.css('option:checked')).getText();
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class AppprofileDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-appprofile-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-appprofile'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
