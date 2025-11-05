<script setup>
import { ref, toRaw } from 'vue'
import {
  Card,
  Button,
  InputText,
  InputNumber,
  InputGroup,
  Textarea,
  DatePicker,
  Select,
  IftaLabel,
  Message,
  ScrollPanel,
} from 'primevue'
import { useToastNotifier } from '@/composables/useToast'

const selectedLabWork = defineModel()

let previousStateLabWork

const { bakeToast } = useToastNotifier()

const colors = ['BLACK', 'BLUE', 'YELLOW', 'BROWN']
const countries = ['UNITED_KINGDOM', 'USA', 'FRANCE', 'SOUTH_KOREA', 'NORTH_KOREA']

const isEditingDescription = ref(false)
const isEditingDetails = ref(false)
const isEditingAuthor = ref(false)

const isCoordinatesXValid = ref(true)
const isCoordinatesYValid = ref(true)
const isDisciplineNameValid = ref(true)
const isDisciplinePracticeHoursValid = ref(true)
const isAuthorNameValid = ref(true)
const isAuthorHairColorValid = ref(true)
const isAuthorBirthdayValid = ref(true)
const isAuthorNationalityValid = ref(true)
const isLocationNameValid = ref(true)
const isLocationXValid = ref(true)
const isLocationYValid = ref(true)
const isLocationZValid = ref(true)

const toggleEditingDescription = () => {
  isEditingDescription.value = !isEditingDescription.value
}

const toggleEditingDetails = () => {
  isEditingDetails.value = !isEditingDetails.value
}

const toggleEditingAuthor = () => {
  isEditingAuthor.value = !isEditingAuthor.value
}

const startEditing = (toggleEditingPanel) => {
  previousStateLabWork = structuredClone(toRaw(selectedLabWork.value))
  toggleEditingPanel()
}

const rollbackEditing = (toggleEditingPanel) => {
  selectedLabWork.value = structuredClone(previousStateLabWork)
  toggleEditingPanel()
}

const updateLabWork = async () => {
  const response = await fetch('http://localhost:8080/lab1/api/labwork', {
    method: 'PATCH',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(selectedLabWork.value),
  })
  const data = await response.json()

  bakeToast(data.string, response.ok)

  return response.ok
}

const validateDescription = async () => {
  if (updateLabWork()) {
    toggleEditingDescription()
  }
}

const validateDetails = async () => {
  isCoordinatesXValid.value = true
  isCoordinatesYValid.value = true
  isDisciplineNameValid.value = true
  isDisciplinePracticeHoursValid.value = true

  if (selectedLabWork.value.coordinates.x === undefined) {
    isCoordinatesXValid.value = false
  }

  if (
    selectedLabWork.value.coordinates.y === undefined ||
    selectedLabWork.value.coordinates.y < -566
  ) {
    isCoordinatesYValid.value = false
  }

  if (selectedLabWork.value.discipline) {
    if (selectedLabWork.value.discipline.name === undefined) {
      isDisciplineNameValid.value = false
    }

    if (
      selectedLabWork.value.discipline.practiceHours === undefined ||
      selectedLabWork.value.discipline.practiceHours < 1
    ) {
      isDisciplinePracticeHoursValid.value = false
    }
  }

  if (
    isCoordinatesXValid.value &&
    isCoordinatesYValid.value &&
    isDisciplineNameValid.value &&
    isDisciplinePracticeHoursValid.value
  ) {
    if (updateLabWork()) {
      toggleEditingDetails()
    }
  }
}

const validateAuthor = () => {
  isAuthorNameValid.value = true
  isAuthorHairColorValid.value = true
  isAuthorBirthdayValid.value = true
  isAuthorNationalityValid.value = true
  isLocationNameValid.value = true
  isLocationXValid.value = true
  isLocationYValid.value = true
  isLocationZValid.value = true

  if (selectedLabWork.value.author.name === undefined) {
    isAuthorNameValid.value = false
  }

  if (selectedLabWork.value.author.hairColor === undefined) {
    isAuthorHairColorValid.value = false
  }

  if (selectedLabWork.value.author.birthday === undefined) {
    isAuthorBirthdayValid.value = false
  }

  if (selectedLabWork.value.author.nationality === undefined) {
    isAuthorNationalityValid.value = false
  }

  if (selectedLabWork.value.author.location) {
    if (
      selectedLabWork.value.author.location.name === undefined ||
      selectedLabWork.value.author.location.name.length > 246
    ) {
      isLocationNameValid.value = false
    }

    if (selectedLabWork.value.author.location.x === undefined) {
      isLocationXValid.value = false
    }

    if (selectedLabWork.value.author.location.y === undefined) {
      isLocationYValid.value = false
    }

    if (selectedLabWork.value.author.location.z === undefined) {
      isLocationZValid.value = false
    }
  }

  if (
    isAuthorNameValid.value &&
    isAuthorHairColorValid &&
    isAuthorBirthdayValid &&
    isAuthorNationalityValid &&
    isLocationNameValid &&
    isLocationXValid &&
    isLocationYValid &&
    isLocationZValid
  ) {
    if (updateLabWork()) {
      toggleEditingAuthor()
    }
  }
}
</script>

<template>
  <Card id="descriptionPanel" class="data-card">
    <template #title>
      <div class="card-title">
        <span>Lab work description</span>
        <Button
          v-if="selectedLabWork && !isEditingDescription"
          icon="pi pi-pencil"
          size="small"
          class="edit-button"
          rounded
          @click="startEditing(toggleEditingDescription)"
        ></Button>
        <Button
          v-if="selectedLabWork && isEditingDescription"
          icon="pi pi-times"
          size="small"
          class="edit-button"
          rounded
          @click="rollbackEditing(toggleEditingDescription)"
        ></Button>
        <Button
          v-if="selectedLabWork && isEditingDescription"
          icon="pi pi-check"
          size="small"
          class="edit-button"
          rounded
          @click="validateDescription"
        ></Button>
      </div>
    </template>
    <template #content>
      <template v-if="isEditingDescription">
        <Textarea variant="filled" v-model="selectedLabWork.description"></Textarea>
      </template>
      <template v-else-if="selectedLabWork">
        <div class="card-details">{{ selectedLabWork.description }}</div>
      </template>
      <template v-else>Select a lab work to read its description</template>
    </template>
  </Card>
  <Card id="detailPanel" class="data-card">
    <template #title>
      <div class="card-title">
        <span>Lab work details</span>
        <Button
          v-if="selectedLabWork && !isEditingDetails"
          icon="pi pi-pencil"
          size="small"
          class="edit-button"
          rounded
          @click="startEditing(toggleEditingDetails)"
        ></Button>
        <Button
          v-if="selectedLabWork && isEditingDetails"
          icon="pi pi-times"
          size="small"
          class="edit-button"
          rounded
          @click="rollbackEditing(toggleEditingDetails)"
        ></Button>
        <Button
          v-if="selectedLabWork && isEditingDetails"
          icon="pi pi-check"
          size="small"
          class="edit-button"
          rounded
          @click="validateDetails"
        ></Button>
      </div>
    </template>
    <template #content>
      <template v-if="isEditingDetails">
        <IftaLabel>
          <InputNumber
            id="coordinatesXInput"
            v-model="selectedLabWork.coordinates.x"
            variant="filled"
            :use-grouping="false"
            :min-fraction-digits="0"
            :max-fraction-digits="5"
          ></InputNumber>
          <Message v-if="!isCoordinatesXValid" severity="error">Field is required</Message>
          <label for="coordinatesXInput">X</label>
        </IftaLabel>
        <IftaLabel>
          <InputNumber
            id="coordinatesYInput"
            v-model="selectedLabWork.coordinates.y"
            variant="filled"
            :use-grouping="false"
            :min-fraction-digits="0"
            :max-fraction-digits="5"
          ></InputNumber>
          <Message v-if="!isCoordinatesYValid" severity="error"
            >Field is required and needs to be greater than or equal to -566</Message
          >
          <label for="coordinatesYInput">Y</label>
        </IftaLabel>
        <template v-if="selectedLabWork.discipline">
          <IftaLabel>
            <InputText
              id="disciplineNameInput"
              v-model="selectedLabWork.discipline.name"
              variant="filled"
            ></InputText>
            <Message v-if="!isDisciplineNameValid" severity="error">Field is required</Message>
            <label for="disciplineNameInput">Discipline name</label>
          </IftaLabel>
          <IftaLabel>
            <InputNumber
              id="disciplinePracticeHoursInput"
              v-model="selectedLabWork.discipline.practiceHours"
              variant="filled"
              :use-grouping="false"
            ></InputNumber>
            <Message v-if="!isDisciplinePracticeHoursValid" severity="error"
              >Field is required and needs to be greater than or equal to 1</Message
            >
            <label for="disciplinePracticeHoursInput">Practice hours</label>
          </IftaLabel>
        </template>
      </template>
      <template v-else-if="selectedLabWork">
        <div class="card-details">
          <strong>Coordinates: </strong>
          <span>X: {{ selectedLabWork.coordinates?.x }}</span>
          <span>Y: {{ selectedLabWork.coordinates?.y }}</span>
        </div>
        <div class="card-details">
          <strong>Discipline: </strong>
          <span>Name: {{ selectedLabWork.discipline?.name }}</span>
          <span>Practice hours: {{ selectedLabWork.discipline?.practiceHours }}</span>
        </div>
      </template>
      <template v-else> Select a lab work to read about its details </template>
    </template>
  </Card>
  <Card id="authorPanel" class="data-card">
    <template #title>
      <div class="card-title">
        <span>About the author</span>
        <Button
          v-if="selectedLabWork && !isEditingAuthor"
          icon="pi pi-pencil"
          size="small"
          class="edit-button"
          rounded
          @click="startEditing(toggleEditingAuthor)"
        ></Button>
        <Button
          v-if="selectedLabWork && isEditingAuthor"
          icon="pi pi-times"
          size="small"
          class="edit-button"
          rounded
          @click="rollbackEditing(toggleEditingAuthor)"
        ></Button>
        <Button
          v-if="selectedLabWork && isEditingAuthor"
          icon="pi pi-check"
          size="small"
          class="edit-button"
          rounded
          @click="validateAuthor"
        ></Button></div
    ></template>
    <template #content>
      <template v-if="isEditingAuthor">
        <ScrollPanel style="width: 100%; height: 250px" class="author-scrollpanel">
          <IftaLabel>
            <InputText
              id="authorNameInput"
              v-model="selectedLabWork.author.name"
              variant="filled"
              size="small"
            ></InputText>
            <Message v-if="!isAuthorNameValid" severity="error">Field is required</Message>
            <label for="authorNameInput">Author name</label>
          </IftaLabel>
          <InputGroup>
            <IftaLabel v-if="selectedLabWork.author.eyeColor">
              <Select
                id="authorEyeColorInput"
                v-model="selectedLabWork.author.eyeColor"
                :options="colors"
                variant="filled"
              ></Select>
              <label for="authorEyeColorInput">Eye color</label>
            </IftaLabel>
            <IftaLabel>
              <Select
                id="authorHairColorInput"
                v-model="selectedLabWork.author.hairColor"
                :options="colors"
                variant="filled"
              ></Select>
              <Message v-if="!isAuthorHairColorValid" severity="error">Field is required</Message>
              <label for="authorEyeColorInput">Hair color</label>
            </IftaLabel>
          </InputGroup>
          <IftaLabel>
            <Select
              id="authorNationalityInput"
              v-model="selectedLabWork.author.nationality"
              :options="countries"
              variant="filled"
            ></Select>
            <Message v-if="!isAuthorNationalityValid" severity="error">Field is required</Message>
            <label for="authorEyeColorInput">Nationality</label>
          </IftaLabel>
          <IftaLabel>
            <DatePicker
              id="authorBirthdayInput"
              v-model="selectedLabWork.author.birthday"
              variant="filled"
              size="small"
            />
            <Message v-if="!isAuthorBirthdayValid" severity="error">Field is required</Message>
            <label for="authorBirthdayInput">Birthday</label>
          </IftaLabel>

          <template v-if="selectedLabWork.author.location">
            <IftaLabel>
              <InputText
                id="locationNameInput"
                v-model="selectedLabWork.author.location.name"
                variant="filled"
                size="small"
              ></InputText>
              <Message v-if="!isLocationNameValid" severity="error"
                >Field is required and can be 246 characters long at most</Message
              >
              <label for="locationNameInput">Location name</label>
            </IftaLabel>
            <InputGroup>
              <IftaLabel>
                <InputNumber
                  id="locationXInput"
                  v-model="selectedLabWork.author.location.x"
                  variant="filled"
                  :use-grouping="false"
                  :min-fraction-digits="0"
                  :max-fraction-digits="5"
                  size="small"
                ></InputNumber>
                <Message v-if="!isLocationXValid" severity="error">Field is required</Message>
                <label for="locationXInput">X</label>
              </IftaLabel>
              <IftaLabel>
                <InputNumber
                  id="locationYInput"
                  v-model="selectedLabWork.author.location.y"
                  variant="filled"
                  :use-grouping="false"
                  :min-fraction-digits="0"
                  :max-fraction-digits="5"
                  size="small"
                ></InputNumber>
                <Message v-if="!isLocationYValid" severity="error">Field is required</Message>
                <label for="locationYInput">Y</label>
              </IftaLabel>
              <IftaLabel>
                <InputNumber
                  id="locationZInput"
                  v-model="selectedLabWork.author.location.z"
                  variant="filled"
                  :use-grouping="false"
                  :min-fraction-digits="0"
                  :max-fraction-digits="5"
                  size="small"
                ></InputNumber>
                <Message v-if="!isLocationZValid" severity="error">Field is required</Message>
                <label for="locationZInput">Z</label>
              </IftaLabel>
            </InputGroup>
          </template>
        </ScrollPanel>
      </template>
      <template v-else-if="selectedLabWork">
        <div class="card-details">
          <span>Name: {{ selectedLabWork.author?.name }}</span>
          <span>Eye color: {{ selectedLabWork.author?.eyeColor }}</span>
          <span>Hair color: {{ selectedLabWork.author?.hairColor }}</span>
          <span>Birthday: {{ selectedLabWork.author?.birthday }}</span>
          <span>Nationality: {{ selectedLabWork.author?.nationality }}</span>
        </div>
        <div class="card-details">
          <strong>Location: </strong>
          <span>Name: {{ selectedLabWork.author?.location.name }}</span>
          <span>X: {{ selectedLabWork.author?.location.x }}</span>
          <span>Y: {{ selectedLabWork.author?.location.y }}</span>
          <span>Z: {{ selectedLabWork.author?.location.z }}</span>
        </div>
      </template>
      <template v-else> Select a lab work to read about its author </template>
    </template>
  </Card>
</template>

<style scoped>
.data-card {
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  height: 100%;

  background: rgba(0, 0, 0, 0.35);
  border-radius: 0%;
  box-shadow: none;
}

.card-title > * {
  padding: 0.25rem;
}

.card-details {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
  padding: 0.25rem;

  font-family: 'Roboto', sans-serif;
}

.edit-button {
  margin-left: auto;
  background: transparent !important;
  border: none !important;
  color: rgba(255, 255, 255, 0.6) !important;
  width: 2rem;
  height: 2rem;
}

.edit-button:hover {
  background: rgba(255, 255, 255, 0.1) !important;
  color: rgba(255, 255, 255, 0.9) !important;
}

.edit-button:active {
  background: rgba(255, 255, 255, 0.2) !important;
  color: rgba(255, 255, 255, 1) !important;
}

.p-textarea,
.p-select,
.p-inputtext,
.p-inputnumber,
.p-datepicker,
.p-message,
.p-inputgroup {
  margin-bottom: 1.5rem;
}

.p-textarea,
.p-select,
.p-inputtext,
:deep(.p-datepicker .p-inputtext),
:deep(.p-inputnumber .p-inputtext) {
  background-color: rgba(0, 0, 0, 0.35) !important;
}
</style>
