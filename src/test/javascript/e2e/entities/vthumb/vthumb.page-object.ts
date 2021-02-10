import { element, by, ElementFinder } from 'protractor';

export class VthumbComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-vthumb div table .btn-danger'));
  title = element.all(by.css('jhi-vthumb div h2#page-heading span')).first();
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

export class VthumbUpdatePage {
  pageTitle = element(by.id('jhi-vthumb-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  creationDateInput = element(by.id('field_creationDate'));
  vthumbUpInput = element(by.id('field_vthumbUp'));
  vthumbDownInput = element(by.id('field_vthumbDown'));

  vquestionSelect = element(by.id('field_vquestion'));
  vanswerSelect = element(by.id('field_vanswer'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setCreationDateInput(creationDate: string): Promise<void> {
    await this.creationDateInput.sendKeys(creationDate);
  }

  async getCreationDateInput(): Promise<string> {
    return await this.creationDateInput.getAttribute('value');
  }

  getVthumbUpInput(): ElementFinder {
    return this.vthumbUpInput;
  }

  getVthumbDownInput(): ElementFinder {
    return this.vthumbDownInput;
  }

  async vquestionSelectLastOption(): Promise<void> {
    await this.vquestionSelect.all(by.tagName('option')).last().click();
  }

  async vquestionSelectOption(option: string): Promise<void> {
    await this.vquestionSelect.sendKeys(option);
  }

  getVquestionSelect(): ElementFinder {
    return this.vquestionSelect;
  }

  async getVquestionSelectedOption(): Promise<string> {
    return await this.vquestionSelect.element(by.css('option:checked')).getText();
  }

  async vanswerSelectLastOption(): Promise<void> {
    await this.vanswerSelect.all(by.tagName('option')).last().click();
  }

  async vanswerSelectOption(option: string): Promise<void> {
    await this.vanswerSelect.sendKeys(option);
  }

  getVanswerSelect(): ElementFinder {
    return this.vanswerSelect;
  }

  async getVanswerSelectedOption(): Promise<string> {
    return await this.vanswerSelect.element(by.css('option:checked')).getText();
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

export class VthumbDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-vthumb-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-vthumb'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
