import { element, by, ElementFinder } from 'protractor';

export class KundeComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-kunde div table .btn-danger'));
  title = element.all(by.css('jhi-kunde div h2#page-heading span')).first();
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

export class KundeUpdatePage {
  pageTitle = element(by.id('jhi-kunde-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  anredeSelect = element(by.id('field_anrede'));
  nameInput = element(by.id('field_name'));
  vornameInput = element(by.id('field_vorname'));
  emailInput = element(by.id('field_email'));
  strasseInput = element(by.id('field_strasse'));
  plzortInput = element(by.id('field_plzort'));
  telefonnrInput = element(by.id('field_telefonnr'));
  ibanInput = element(by.id('field_iban'));
  createdInput = element(by.id('field_created'));

  userSelect = element(by.id('field_user'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setAnredeSelect(anrede: string): Promise<void> {
    await this.anredeSelect.sendKeys(anrede);
  }

  async getAnredeSelect(): Promise<string> {
    return await this.anredeSelect.element(by.css('option:checked')).getText();
  }

  async anredeSelectLastOption(): Promise<void> {
    await this.anredeSelect.all(by.tagName('option')).last().click();
  }

  async setNameInput(name: string): Promise<void> {
    await this.nameInput.sendKeys(name);
  }

  async getNameInput(): Promise<string> {
    return await this.nameInput.getAttribute('value');
  }

  async setVornameInput(vorname: string): Promise<void> {
    await this.vornameInput.sendKeys(vorname);
  }

  async getVornameInput(): Promise<string> {
    return await this.vornameInput.getAttribute('value');
  }

  async setEmailInput(email: string): Promise<void> {
    await this.emailInput.sendKeys(email);
  }

  async getEmailInput(): Promise<string> {
    return await this.emailInput.getAttribute('value');
  }

  async setStrasseInput(strasse: string): Promise<void> {
    await this.strasseInput.sendKeys(strasse);
  }

  async getStrasseInput(): Promise<string> {
    return await this.strasseInput.getAttribute('value');
  }

  async setPlzortInput(plzort: string): Promise<void> {
    await this.plzortInput.sendKeys(plzort);
  }

  async getPlzortInput(): Promise<string> {
    return await this.plzortInput.getAttribute('value');
  }

  async setTelefonnrInput(telefonnr: string): Promise<void> {
    await this.telefonnrInput.sendKeys(telefonnr);
  }

  async getTelefonnrInput(): Promise<string> {
    return await this.telefonnrInput.getAttribute('value');
  }

  async setIbanInput(iban: string): Promise<void> {
    await this.ibanInput.sendKeys(iban);
  }

  async getIbanInput(): Promise<string> {
    return await this.ibanInput.getAttribute('value');
  }

  async setCreatedInput(created: string): Promise<void> {
    await this.createdInput.sendKeys(created);
  }

  async getCreatedInput(): Promise<string> {
    return await this.createdInput.getAttribute('value');
  }

  async userSelectLastOption(): Promise<void> {
    await this.userSelect.all(by.tagName('option')).last().click();
  }

  async userSelectOption(option: string): Promise<void> {
    await this.userSelect.sendKeys(option);
  }

  getUserSelect(): ElementFinder {
    return this.userSelect;
  }

  async getUserSelectedOption(): Promise<string> {
    return await this.userSelect.element(by.css('option:checked')).getText();
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

export class KundeDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-kunde-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-kunde'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
