<script setup>
import {
  Button,
  IftaLabel,
  InputText,
  InputNumber,
  Select,
  SelectButton,
  Textarea,
  Stepper,
  StepList,
  StepPanels,
  StepPanel,
  Step,
  DatePicker,
  FileUpload,
  DataTable,
  Column,
} from 'primevue'
import { ref } from 'vue'
import { useToastNotifier } from '@/composables/useToast'

defineProps({
  disciplines: {
    type: Array,
  },
  coordinates: {
    type: Array,
  },
  authors: {
    type: Array,
  },
  locations: {
    type: Array,
  },
  uploads: {
    type: Array,
  },
})

const emit = defineEmits(['closeForm'])

const emitCloseForm = () => {
  emit('closeForm')
}

const labworkName = ref()
const labworkDescription = ref()
const labworkDifficulty = ref()
const labworkMinimalPoint = ref()
const labworkAveragePoint = ref()

const disciplineName = ref()
const disciplinePracticeHours = ref()

const coordinatesX = ref()
const coordinatesY = ref()

const authorName = ref()
const authorEyeColor = ref()
const authorHairColor = ref()
const authorBirthday = ref()
const authorNationality = ref()

const locationName = ref()
const locationX = ref()
const locationY = ref()
const locationZ = ref()

const difficulties = ['VERY_EASY', 'NORMAL', 'INSANE', 'IMPOSSIBLE']
const colors = ['BLACK', 'BLUE', 'YELLOW', 'BROWN']
const countries = ['UNITED_KINGDOM', 'USA', 'FRANCE', 'SOUTH_KOREA', 'NORTH_KOREA']

const modeOptions = ref([
  { label: 'Select existing', value: 'existing' },
  { label: 'Create new', value: 'new' },
])

const coordinatesMode = ref('existing')
const disciplineMode = ref('existing')
const authorMode = ref('existing')
const locationMode = ref('existing')

const selectedCoordinates = ref()
const selectedDiscipline = ref()
const selectedAuthor = ref()
const selectedLocation = ref()

const { bakeToast } = useToastNotifier()

const fieldConstraintViolationMessage = (fieldName) => {
  return `Field constraints violated: ${fieldName}`
}

const missingFileDataMessage = (objectName) => {
  return `Missing data in uploaded file for subobject: ${objectName}`
}

const validateLabwork = (labwork) => {
  if (labwork.name === undefined || labwork.name === '' || typeof labwork.name !== 'string') {
    bakeToast(fieldConstraintViolationMessage('lab work name'), false)
    return false
  }

  if (
    labwork.description !== undefined &&
    labwork.description !== null &&
    typeof labwork.description !== 'string'
  ) {
    bakeToast(fieldConstraintViolationMessage('lab work description'), false)
    return false
  }

  if (
    labwork.difficulty !== undefined &&
    labwork.difficulty !== null &&
    !difficulties.includes(labwork.difficulty)
  ) {
    bakeToast(fieldConstraintViolationMessage('lab work difficulty'), false)
    return false
  }

  if (
    labwork.minimalPoint !== undefined &&
    labwork.minimalPoint !== null &&
    (typeof labwork.minimalPoint !== 'number' || labwork.minimalPoint <= 0)
  ) {
    bakeToast(fieldConstraintViolationMessage('lab work minimal point'), false)
    return false
  }

  if (
    labwork.averagePoint === undefined ||
    labwork.averagePoint === null ||
    typeof labwork.averagePoint !== 'number' ||
    labwork.averagePoint <= 0
  ) {
    bakeToast(fieldConstraintViolationMessage('lab work average point'), false)
    return false
  }

  return true
}

const validateDiscipline = (discipline) => {
  if (
    discipline.name === undefined ||
    discipline.name === '' ||
    typeof discipline.name !== 'string'
  ) {
    bakeToast(fieldConstraintViolationMessage('discipline name'), false)
    return false
  }

  if (
    discipline.practiceHours === undefined ||
    discipline.practiceHours === null ||
    typeof discipline.practiceHours !== 'number' ||
    discipline.practiceHours < 1
  ) {
    bakeToast(fieldConstraintViolationMessage('discipline practice hours'), false)
    return false
  }

  return true
}

const validateCoordinates = (coordinates) => {
  if (coordinates.x === undefined || coordinates.x === null || typeof coordinates.x !== 'number') {
    bakeToast(fieldConstraintViolationMessage('coordinates X'), false)
    return false
  }

  if (
    coordinates.y === undefined ||
    coordinates.y === null ||
    typeof coordinates.y !== 'number' ||
    coordinates.y < -566
  ) {
    bakeToast(fieldConstraintViolationMessage('coordinates Y'), false)
    return false
  }

  return true
}

const validateAuthor = (author) => {
  if (author.name === undefined || author.name === '' || typeof author.name !== 'string') {
    bakeToast(fieldConstraintViolationMessage('author name'), false)
    return false
  }

  if (author.hairColor === undefined || !colors.includes(author.hairColor)) {
    bakeToast(fieldConstraintViolationMessage('author hair color'), false)
    return false
  }

  if (author.eyeColor !== undefined && !colors.includes(author.eyeColor)) {
    bakeToast(fieldConstraintViolationMessage('author eye color'), false)
    return false
  }

  if (author.birthday === undefined) {
    bakeToast(fieldConstraintViolationMessage('author birthday'), false)
    return false
  }

  if (author.nationality === undefined || !countries.includes(author.nationality)) {
    bakeToast(fieldConstraintViolationMessage('author nationality'), false)
    return false
  }

  return true
}

const validateLocation = (location) => {
  if (
    location.name === undefined ||
    location.name === '' ||
    typeof location.name !== 'string' ||
    location.name.length > 246
  ) {
    bakeToast(fieldConstraintViolationMessage('location name'), false)
    return false
  }

  if (location.x === undefined || location.x === null || typeof location.x !== 'number') {
    bakeToast(fieldConstraintViolationMessage('location X'), false)
    return false
  }

  if (location.y === undefined || location.y === null || typeof location.y !== 'number') {
    bakeToast(fieldConstraintViolationMessage('location Y'), false)
    return false
  }

  if (location.z === undefined || location.z === null || typeof location.z !== 'number') {
    bakeToast(fieldConstraintViolationMessage('location Z'), false)
    return false
  }

  return true
}

const validateStage1 = (activateCallback) => {
  const labwork = {
    name: labworkName.value,
    minimalPoint: labworkMinimalPoint.value,
    averagePoint: labworkAveragePoint.value,
  }

  if (validateLabwork(labwork)) {
    activateCallback('2')
  }
}

const validateStage2 = (activateCallback) => {
  if (selectedDiscipline.value && disciplineMode.value === 'existing') {
    activateCallback('3')
    return
  }

  const discipline = {
    name: disciplineName.value,
    practiceHours: disciplinePracticeHours.value,
  }

  if (validateDiscipline(discipline)) {
    activateCallback('3')
  }
}

const validateStage3 = (activateCallback) => {
  if (selectedCoordinates.value && coordinatesMode.value === 'existing') {
    activateCallback('4')
    return
  }

  const coordinates = {
    x: coordinatesX.value,
    y: coordinatesY.value,
  }

  if (validateCoordinates(coordinates)) {
    activateCallback('4')
  }
}

const validateStage4 = (activateCallback) => {
  if (selectedAuthor.value && authorMode.value === 'existing') {
    activateCallback('5')
    return
  }

  const author = {
    name: authorName.value,
    hairColor: authorHairColor.value,
    birthday: authorBirthday.value,
    nationality: authorNationality.value,
  }

  if (validateAuthor(author)) {
    activateCallback('5')
  }
}

const validateStage5 = () => {
  if (selectedLocation.value && locationMode.value === 'existing') {
    createEntry()
    return
  }

  const location = {
    name: locationName.value,
    x: locationX.value,
    y: locationY.value,
    z: locationZ.value,
  }

  if (validateLocation(location)) {
    createEntry()
  }
}

async function createEntry(entry = null) {
  let body
  if (entry !== null) {
    body = entry
  } else {
    body = {
      name: labworkName.value,
      description: labworkDescription.value,
      difficulty: labworkDifficulty.value,
      minimalPoint: labworkMinimalPoint.value,
      averagePoint: labworkAveragePoint.value,
      discipline: selectedDiscipline.value
        ? selectedDiscipline.value
        : {
            name: disciplineName.value,
            practiceHours: disciplinePracticeHours.value,
          },
      coordinates: selectedCoordinates.value
        ? selectedCoordinates.value
        : {
            x: coordinatesX.value,
            y: coordinatesY.value,
          },
      author: selectedAuthor.value
        ? selectedAuthor.value
        : {
            name: authorName.value,
            eyeColor: authorEyeColor.value,
            hairColor: authorHairColor.value,
            birthday: new Date(authorBirthday.value).toISOString().slice(0, -1),
            nationality: authorNationality.value,
            location: selectedLocation.value
              ? selectedLocation.value
              : {
                  name: locationName.value,
                  x: locationX.value,
                  y: locationY.value,
                  z: locationZ.value,
                },
          },
    }
  }

  const response = await fetch('http://localhost:8080/lab1/api/labwork', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(body),
  })
  const data = await response.json()

  bakeToast(data.string, response.ok)

  if (response.ok) {
    emitCloseForm()
  }
}

async function createBatch(entries) {
  const body = entries
  const response = await fetch('http://localhost:8080/lab1/api/labwork/batch', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(body),
  })
  const data = await response.json()

  bakeToast(data.string, response.ok)
}

const resolveDiscipline = (entry) => {
  if (!entry.discipline) {
    bakeToast(missingFileDataMessage('discipline'), false)
    return false
  }
  return validateDiscipline(entry.discipline)
}

const resolveCoordinates = (entry) => {
  if (!entry.coordinates) {
    bakeToast(missingFileDataMessage('coordinates'), false)
    return false
  }
  return validateCoordinates(entry.coordinates)
}

const resolveAuthor = (entry) => {
  if (!entry.author) {
    bakeToast(missingFileDataMessage('author'), false)
    return false
  }
  return validateAuthor(entry.author)
}

const resolveLocation = (entry) => {
  if (entry.author && !entry.author.location) {
    bakeToast(missingFileDataMessage('location'), false)
    return false
  }
  return validateLocation(entry.author.location)
}

const onFileSelect = async (event) => {
  const file = event.files[0]

  try {
    const text = await file.text()
    const entries = JSON.parse(text)

    if (!Array.isArray(entries)) {
      bakeToast('Uploaded file does not contain an array of objects', false)
    } else {
      let valid = true
      for (const entry of entries) {
        if (
          !(
            validateLabwork(entry) &&
            resolveDiscipline(entry) &&
            resolveCoordinates(entry) &&
            resolveAuthor(entry) &&
            resolveLocation(entry)
          )
        ) {
          valid = false
          break
        }
      }

      if (valid) {
        createBatch(entries)
      }
    }
  } catch {
    bakeToast('Failed to process the file: uploaded file is not of JSON format.', false)
  }
}
</script>

<template>
  <Stepper value="1" linear>
    <StepList>
      <Step value="1">Lab work parameters</Step>
      <Step value="2">Discipline</Step>
      <Step value="3">Coordinates</Step>
      <Step value="4">Author</Step>
      <Step value="5">Location</Step>
    </StepList>
    <StepPanels>
      <StepPanel v-slot="{ activateCallback }" value="1">
        <div style="display: flex; justify-content: space-evenly">
          <div style="display: flex; flex-direction: column; align-items: flex-start">
            <IftaLabel>
              <InputText
                id="formNameInput"
                v-model="labworkName"
                variant="filled"
                placeholder="Required"
              ></InputText>
              <label for="formNameInput">Name</label>
            </IftaLabel>

            <IftaLabel>
              <Textarea
                id="formDescriptionInput"
                v-model="labworkDescription"
                variant="filled"
              ></Textarea>
              <label for="formDescriptionInput">Description</label>
            </IftaLabel>

            <IftaLabel>
              <Select
                id="formDifficultyInput"
                v-model="labworkDifficulty"
                :options="difficulties"
                variant="filled"
              ></Select>
              <label for="formDifficultyInput">Difficulty</label>
            </IftaLabel>

            <IftaLabel>
              <InputNumber
                id="formMinimalPointInput"
                v-model="labworkMinimalPoint"
                variant="filled"
                :use-grouping="false"
                :min-fraction-digits="0"
                :max-fraction-digits="5"
                placeholder="Greater than 0"
              ></InputNumber>
              <label for="formMinimalPointInput">Minimal point</label>
            </IftaLabel>

            <IftaLabel>
              <InputNumber
                id="formAveragePointInput"
                v-model="labworkAveragePoint"
                variant="filled"
                :use-grouping="false"
                :min-fraction-digits="0"
                :max-fraction-digits="5"
                placeholder="Required; value > 0"
              ></InputNumber>
              <label for="formAveragePointInput">Average point</label>
            </IftaLabel>
          </div>

          <div>
            <FileUpload
              mode="basic"
              @select="onFileSelect"
              chooseLabel="Upload a file"
              :custom-upload="true"
            />
            <DataTable
              id="uploadTable"
              :value="uploads"
              scrollable
              scroll-height="300px"
              style="margin-top: 1rem"
            >
              <Column field="id" header="ID"></Column>
              <Column field="status" header="Status"></Column>
              <Column field="objectsAdded" header="Objects added"></Column>
            </DataTable>
          </div>
        </div>

        <div style="display: flex; justify-content: flex-end">
          <Button label="Next" @click="validateStage1(activateCallback)"></Button>
        </div>
      </StepPanel>

      <StepPanel v-slot="{ activateCallback }" value="2">
        <SelectButton
          v-model="disciplineMode"
          :options="modeOptions"
          option-label="label"
          option-value="value"
        ></SelectButton>

        <div v-if="disciplineMode === 'existing'">
          <Select
            v-model="selectedDiscipline"
            :options="disciplines"
            placeholder="Select existing discipline"
            variant="filled"
            option-label="name"
          ></Select>
        </div>

        <div v-else>
          <IftaLabel>
            <InputText
              id="formDisciplineNameInput"
              v-model="disciplineName"
              variant="filled"
              placeholder="Required"
            ></InputText>
            <label for="formDisciplineNameInput">Discipline name</label>
          </IftaLabel>

          <IftaLabel>
            <InputNumber
              id="formDisciplinePracticeHoursInput"
              v-model="disciplinePracticeHours"
              variant="filled"
              :use-grouping="false"
              placeholder="Required; value >= 1"
            ></InputNumber>
            <label for="formDisciplinePracticeHoursInput">Practice hours</label>
          </IftaLabel>
        </div>

        <div style="display: flex; justify-content: space-between">
          <Button label="Back" @click="activateCallback('1')"></Button>
          <Button label="Next" @click="validateStage2(activateCallback)"></Button>
        </div>
      </StepPanel>

      <StepPanel v-slot="{ activateCallback }" value="3">
        <SelectButton
          v-model="coordinatesMode"
          :options="modeOptions"
          option-label="label"
          option-value="value"
        ></SelectButton>

        <div v-if="coordinatesMode === 'existing'">
          <Select
            v-model="selectedCoordinates"
            :options="coordinates"
            placeholder="Select existing coordinates"
            variant="filled"
          >
            <template #value="slotProps">
              <div v-if="slotProps.value">
                X: {{ slotProps.value.x }}, Y: {{ slotProps.value.y }}
              </div>
              <div v-else>
                {{ slotProps.placeholder }}
              </div>
            </template>
            <template #option="slotProps">
              <div>X: {{ slotProps.option.x }}, Y: {{ slotProps.option.y }}</div>
            </template>
          </Select>
        </div>

        <div v-else>
          <IftaLabel>
            <InputNumber
              id="formCoordinatesXInput"
              v-model="coordinatesX"
              variant="filled"
              :use-grouping="false"
              :min-fraction-digits="0"
              :max-fraction-digits="5"
              placeholder="Required"
            ></InputNumber>
            <label for="formCoordinatesXInput">X coordinate</label>
          </IftaLabel>

          <IftaLabel>
            <InputNumber
              id="formCoordinatesYInput"
              v-model="coordinatesY"
              variant="filled"
              :use-grouping="false"
              :min-fraction-digits="0"
              :max-fraction-digits="5"
              placeholder="Required; value >= -566"
            ></InputNumber>
            <label for="formCoordinatesYInput">Y coordinate</label>
          </IftaLabel>
        </div>

        <div style="display: flex; justify-content: space-between">
          <Button label="Back" @click="activateCallback('2')"></Button>
          <Button label="Next" @click="validateStage3(activateCallback)"></Button>
        </div>
      </StepPanel>

      <StepPanel v-slot="{ activateCallback }" value="4">
        <SelectButton
          v-model="authorMode"
          :options="modeOptions"
          option-label="label"
          option-value="value"
        ></SelectButton>

        <div v-if="authorMode === 'existing'">
          <Select
            v-model="selectedAuthor"
            :options="authors"
            placeholder="Select existing author"
            variant="filled"
            option-label="name"
          >
          </Select>
        </div>

        <div v-else>
          <IftaLabel>
            <InputText
              id="formAuthorNameInput"
              v-model="authorName"
              variant="filled"
              placeholder="Required"
            ></InputText>
            <label for="formAuthorNameInput">Author name</label>
          </IftaLabel>

          <IftaLabel>
            <Select
              id="formAuthorEyeColorInput"
              v-model="authorEyeColor"
              :options="colors"
              variant="filled"
            ></Select>
            <label for="formAuthorEyeColorInput">Eye color</label>
          </IftaLabel>

          <IftaLabel>
            <Select
              id="formAuthorHairColorInput"
              v-model="authorHairColor"
              :options="colors"
              variant="filled"
              placeholder="Required"
            ></Select>
            <label for="formAuthorHairColorInput">Hair color</label>
          </IftaLabel>

          <IftaLabel>
            <DatePicker
              id="formAuthorBirthdayInput"
              v-model="authorBirthday"
              variant="filled"
              placeholder="Required"
            ></DatePicker>
            <label for="formAuthorBirthdayInput">Birthday</label>
          </IftaLabel>

          <IftaLabel>
            <Select
              id="formAuthorNationalityInput"
              v-model="authorNationality"
              :options="countries"
              variant="filled"
              placeholder="Required"
            ></Select>
            <label for="formAuthorNationalityInput">Nationality</label>
          </IftaLabel>
        </div>

        <div style="display: flex; justify-content: space-between">
          <Button label="Back" @click="activateCallback('3')"></Button>
          <Button
            :label="selectedAuthor ? 'Create' : 'Next'"
            @click="selectedAuthor ? createEntry() : validateStage4(activateCallback)"
          ></Button>
        </div>
      </StepPanel>

      <StepPanel v-slot="{ activateCallback }" value="5">
        <SelectButton
          v-model="locationMode"
          :options="modeOptions"
          option-label="label"
          option-value="value"
        ></SelectButton>

        <div v-if="locationMode === 'existing'">
          <Select
            v-model="selectedLocation"
            :options="locations"
            placeholder="Select existing location"
            variant="filled"
            option-label="name"
          >
          </Select>
        </div>

        <div v-else>
          <IftaLabel>
            <InputText
              id="formLocationNameInput"
              v-model="locationName"
              variant="filled"
              placeholder="Required; 246 characters at most"
            ></InputText>
            <label for="formLocationNameInput">Location name</label>
          </IftaLabel>

          <IftaLabel>
            <InputNumber
              id="formLocationXInput"
              v-model="locationX"
              variant="filled"
              :use-grouping="false"
              placeholder="Required"
            ></InputNumber>
            <label for="formLocationXInput">X coordinate</label>
          </IftaLabel>

          <IftaLabel>
            <InputNumber
              id="formLocationYInput"
              v-model="locationY"
              variant="filled"
              :use-grouping="false"
              :min-fraction-digits="0"
              :max-fraction-digits="5"
              placeholder="Required"
            ></InputNumber>
            <label for="formLocationYInput">Y coordinate</label>
          </IftaLabel>

          <IftaLabel>
            <InputNumber
              id="formLocationZInput"
              v-model="locationZ"
              variant="filled"
              :use-grouping="false"
              :min-fraction-digits="0"
              :max-fraction-digits="5"
              placeholder="Required"
            ></InputNumber>
            <label for="formLocationZInput">Z coordinate</label>
          </IftaLabel>
        </div>

        <div style="display: flex; justify-content: space-between">
          <Button label="Back" @click="activateCallback('4')"></Button>
          <Button label="Create" severity="success" @click="validateStage5"></Button>
        </div>
      </StepPanel>
    </StepPanels>
  </Stepper>
</template>

<style scoped>
.p-textarea,
.p-select,
.p-selectbutton,
.p-inputtext,
.p-inputnumber,
.p-datepicker,
.p-message {
  margin-bottom: 1.5rem;
}

:deep(.p-step-title) {
  font-family: 'Tektur', sans-serif !important;
}
</style>
